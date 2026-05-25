package com.example.multimodule.core.domain.repository

import com.example.multimodule.core.domain.model.Task
import com.example.multimodule.core.domain.model.TaskList
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTaskLists(): Flow<List<TaskList>>
    fun getActiveTasksForList(listId: String): Flow<List<Task>>
    fun getTasksForList(listId: String): Flow<List<Task>>
    
    suspend fun createTaskList(title: String): String
    suspend fun deleteTaskList(listId: String)
    suspend fun renameTaskList(listId: String, newTitle: String)
    
    suspend fun createTask(listId: String, title: String, order: Int): String
    suspend fun deleteTask(taskId: String)
    suspend fun updateTaskCompletion(taskId: String, isCompleted: Boolean)
    suspend fun renameTask(taskId: String, newTitle: String)
    
    suspend fun syncWithBackend()
}
