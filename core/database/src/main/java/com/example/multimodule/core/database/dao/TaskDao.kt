package com.example.multimodule.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.multimodule.core.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // SQL query with auto-sorting: non-completed tasks with status PENDING or COMPLETED
    @Query("SELECT * FROM tasks WHERE listId = :listId AND isCompleted = 0 AND syncStatus IN ('PENDING', 'COMPLETED') ORDER BY `order` ASC")
    fun getActiveTasksForList(listId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE listId = :listId ORDER BY isCompleted ASC, `order` ASC")
    fun getTasksForList(listId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE syncStatus = 'PENDING'")
    suspend fun getPendingTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Query("UPDATE tasks SET title = :title, syncStatus = :syncStatus WHERE id = :id")
    suspend fun updateTaskTitle(id: String, title: String, syncStatus: String)

    @Query("UPDATE tasks SET isCompleted = :isCompleted, syncStatus = :syncStatus WHERE id = :id")
    suspend fun updateTaskCompletion(id: String, isCompleted: Boolean, syncStatus: String)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: String)

    @Query("DELETE FROM tasks WHERE listId = :listId")
    suspend fun deleteTasksByListId(listId: String)

    @Query("DELETE FROM tasks")
    suspend fun clearAll()
}
