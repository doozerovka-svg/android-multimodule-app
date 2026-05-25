package com.example.multimodule.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multimodule.core.domain.model.TaskList
import com.example.multimodule.core.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    // Reactive stream directly from Room database
    val taskLists: StateFlow<List<TaskList>> = taskRepository.getTaskLists()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun createList(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            taskRepository.createTaskList(title.trim())
        }
    }

    fun deleteList(id: String) {
        viewModelScope.launch {
            taskRepository.deleteTaskList(id)
        }
    }

    fun renameList(id: String, newTitle: String) {
        if (newTitle.isBlank()) return
        viewModelScope.launch {
            taskRepository.renameTaskList(id, newTitle.trim())
        }
    }
}
