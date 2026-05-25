package com.example.multimodule.feature.dashboard.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multimodule.core.domain.model.Task
import com.example.multimodule.core.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Extract listId from navigation argument
    val listId: String = savedStateHandle.get<String>("listId")
        ?: throw IllegalArgumentException("listId required")

    // Retrieve list title reactively
    val listTitle: StateFlow<String> = taskRepository.getTaskLists()
        .map { lists ->
            lists.find { it.id == listId }?.title ?: "Список задач"
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Список задач"
        )

    // Retrieve tasks, sorted dynamically: uncompleted tasks first, completed tasks at the bottom
    val tasks: StateFlow<List<Task>> = taskRepository.getTasksForList(listId)
        .map { tasksList ->
            tasksList.sortedWith(
                compareBy<Task> { it.isCompleted }
                    .thenBy { it.order }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun createTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val currentTasks = tasks.value
            val nextOrder = (currentTasks.maxOfOrNull { it.order } ?: 0) + 1
            taskRepository.createTask(listId, title.trim(), nextOrder)
        }
    }

    fun toggleTaskCompletion(taskId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            taskRepository.updateTaskCompletion(taskId, isCompleted)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }

    fun renameTask(taskId: String, newTitle: String) {
        if (newTitle.isBlank()) return
        viewModelScope.launch {
            taskRepository.renameTask(taskId, newTitle.trim())
        }
    }
    
    fun sync() {
        viewModelScope.launch {
            taskRepository.syncWithBackend()
        }
    }
}
