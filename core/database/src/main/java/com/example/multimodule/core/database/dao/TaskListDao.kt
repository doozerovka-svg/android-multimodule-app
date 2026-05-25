package com.example.multimodule.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.multimodule.core.database.entity.TaskListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskListDao {
    @Query("SELECT * FROM task_lists ORDER BY createdAt DESC")
    fun getTaskListsFlow(): Flow<List<TaskListEntity>>

    @Query("SELECT * FROM task_lists WHERE syncStatus = 'PENDING'")
    suspend fun getPendingTaskLists(): List<TaskListEntity>

    @Query("SELECT * FROM task_lists WHERE id = :id")
    suspend fun getTaskListById(id: String): TaskListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskList(taskList: TaskListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskLists(taskLists: List<TaskListEntity>)

    @Query("UPDATE task_lists SET title = :title, syncStatus = :syncStatus WHERE id = :id")
    suspend fun updateTaskListTitle(id: String, title: String, syncStatus: String)

    @Query("DELETE FROM task_lists WHERE id = :id")
    suspend fun deleteTaskListById(id: String)

    @Query("DELETE FROM task_lists")
    suspend fun clearAll()
}
