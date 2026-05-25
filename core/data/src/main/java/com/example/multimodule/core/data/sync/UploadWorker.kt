package com.example.multimodule.core.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.multimodule.core.data.mapper.toDto
import com.example.multimodule.core.data.mapper.toEntity
import com.example.multimodule.core.database.dao.TaskDao
import com.example.multimodule.core.database.dao.TaskListDao
import com.example.multimodule.core.domain.model.SyncStatus
import com.example.multimodule.core.network.ApiService
import com.example.multimodule.core.network.model.SyncRequestDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val taskListDao: TaskListDao,
    private val taskDao: TaskDao,
    private val apiService: ApiService
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        try {
            // 1. Gather all local PENDING changes
            val pendingLists = taskListDao.getPendingTaskLists()
            val pendingTasks = taskDao.getPendingTasks()

            // If there's nothing to sync, finish successfully
            if (pendingLists.isEmpty() && pendingTasks.isEmpty()) {
                return Result.success()
            }

            // 2. Prepare SyncRequest DTO
            val syncRequest = SyncRequestDto(
                pendingLists = pendingLists.map { it.toDto() },
                pendingTasks = pendingTasks.map { it.toDto() }
            )

            // 3. Make synchronous Retrofit network call
            val response = apiService.batchSync(syncRequest)

            // 4. Mark successfully synced records to COMPLETED status
            val syncedLists = response.syncedLists.map { it.toEntity(SyncStatus.COMPLETED) }
            val syncedTasks = response.syncedTasks.map { it.toEntity(SyncStatus.COMPLETED) }

            taskListDao.insertTaskLists(syncedLists)
            taskDao.insertTasks(syncedTasks)

            return Result.success()
        } catch (e: IOException) {
            // Network failure - trigger retry logic based on WorkManager config
            return Result.retry()
        } catch (e: Exception) {
            // Fatal programming or server error - fail without infinite loop retries
            return Result.failure()
        }
    }
}
