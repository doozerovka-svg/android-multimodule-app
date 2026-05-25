package com.example.multimodule.core.data.repository

import com.example.multimodule.core.data.mapper.toDomain
import com.example.multimodule.core.data.mapper.toDto
import com.example.multimodule.core.data.mapper.toEntity
import com.example.multimodule.core.database.dao.TaskDao
import com.example.multimodule.core.database.dao.TaskListDao
import com.example.multimodule.core.database.dao.UserDao
import com.example.multimodule.core.database.entity.TaskEntity
import com.example.multimodule.core.database.entity.TaskListEntity
import com.example.multimodule.core.domain.model.SyncStatus
import com.example.multimodule.core.domain.model.Task
import com.example.multimodule.core.domain.model.TaskList
import com.example.multimodule.core.domain.repository.TaskRepository
import com.example.multimodule.core.network.ApiService
import com.example.multimodule.core.network.model.SyncRequestDto
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.multimodule.core.data.sync.UploadWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskListDao: TaskListDao,
    private val taskDao: TaskDao,
    private val userDao: UserDao,
    private val apiService: ApiService,
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context
) : TaskRepository {

    override fun getTaskLists(): Flow<List<TaskList>> {
        return taskListDao.getTaskListsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getActiveTasksForList(listId: String): Flow<List<Task>> {
        return taskDao.getActiveTasksForList(listId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTasksForList(listId: String): Flow<List<Task>> {
        return taskDao.getTasksForList(listId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    private suspend fun getUserId(): String {
        return userDao.getLoggedInUserFlow().firstOrNull()?.id ?: "local_user"
    }

    private fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "UploadSyncWork",
            ExistingWorkPolicy.REPLACE,
            syncWorkRequest
        )
    }

    override suspend fun createTaskList(title: String): String {
        val id = UUID.randomUUID().toString()
        val userId = getUserId()
        val localList = TaskListEntity(
            id = id,
            userId = userId,
            title = title,
            createdAt = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING.name
        )
        // Instant UI feedback: save locally first
        taskListDao.insertTaskList(localList)
        scheduleSync()
        return id
    }

    override suspend fun deleteTaskList(listId: String) {
        taskListDao.deleteTaskListById(listId)
        taskDao.deleteTasksByListId(listId)
        scheduleSync()
    }

    override suspend fun renameTaskList(listId: String, newTitle: String) {
        taskListDao.updateTaskListTitle(listId, newTitle, SyncStatus.PENDING.name)
        scheduleSync()
    }

    override suspend fun createTask(listId: String, title: String, order: Int): String {
        val id = UUID.randomUUID().toString()
        val localTask = TaskEntity(
            id = id,
            listId = listId,
            title = title,
            isCompleted = false,
            order = order,
            syncStatus = SyncStatus.PENDING.name
        )
        // Instant UI feedback: save locally first
        taskDao.insertTask(localTask)
        scheduleSync()
        return id
    }

    override suspend fun deleteTask(taskId: String) {
        taskDao.deleteTaskById(taskId)
        scheduleSync()
    }

    override suspend fun updateTaskCompletion(taskId: String, isCompleted: Boolean) {
        taskDao.updateTaskCompletion(taskId, isCompleted, SyncStatus.PENDING.name)
        scheduleSync()
    }

    override suspend fun renameTask(taskId: String, newTitle: String) {
        taskDao.updateTaskTitle(taskId, newTitle, SyncStatus.PENDING.name)
        scheduleSync()
    }

    override suspend fun syncWithBackend() {
        try {
            // 1. Gather all local unsynced (PENDING) modifications
            val pendingLists = taskListDao.getPendingTaskLists()
            val pendingTasks = taskDao.getPendingTasks()

            // 2. Wrap into SyncRequest DTO
            val syncRequest = SyncRequestDto(
                pendingLists = pendingLists.map { it.toDto() },
                pendingTasks = pendingTasks.map { it.toDto() }
            )

            // 3. Post batch sync request to NestJS backend
            val syncResponse = apiService.batchSync(syncRequest)

            // 4. Update successfully synced local records to COMPLETED status
            val syncedLists = syncResponse.syncedLists.map { it.toEntity(SyncStatus.COMPLETED) }
            val syncedTasks = syncResponse.syncedTasks.map { it.toEntity(SyncStatus.COMPLETED) }
            
            taskListDao.insertTaskLists(syncedLists)
            taskDao.insertTasks(syncedTasks)

            // 5. Fetch full state from backend to refresh local cache (ensure consistency)
            val remoteLists = apiService.getTaskLists()
            taskListDao.clearAll()
            taskListDao.insertTaskLists(remoteLists.map { it.toEntity(SyncStatus.COMPLETED) })

            taskDao.clearAll()
            for (list in remoteLists) {
                val remoteTasks = apiService.getTasksForList(list.id)
                taskDao.insertTasks(remoteTasks.map { it.toEntity(SyncStatus.COMPLETED) })
            }
        } catch (e: Exception) {
            // Log or handle network issues gracefully. As an offline-first app,
            // local state remains unchanged, and sync will retry during next WorkManager cycle.
            e.printStackTrace()
        }
    }
}
