package com.example.multimodule.feature.ai.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multimodule.core.domain.repository.AiPredictRepository
import com.example.multimodule.core.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val aiPredictRepository: AiPredictRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AiUiState>(AiUiState.Idle)
    val uiState: StateFlow<AiUiState> = _uiState.asStateFlow()

    private val loadingTips = listOf(
        "ИИ обдумывает ваш запрос...",
        "Анализируем контекст задачи...",
        "Формируем оптимальный список дел...",
        "Почти готово, структурируем задачи..."
    )

    fun generateListWithAi(prompt: String) {
        if (prompt.isBlank()) {
            _uiState.value = AiUiState.Error("Запрос не может быть пустым")
            return
        }

        viewModelScope.launch {
            _uiState.value = AiUiState.Loading(loadingTips[0])
            
            // Launch dynamic loading message cycler in background
            val cyclerJob = launch {
                var index = 0
                while (true) {
                    delay(2000)
                    index = (index + 1) % loadingTips.size
                    _uiState.value = AiUiState.Loading(loadingTips[index])
                }
            }

            try {
                // Call API via repository
                val generated = aiPredictRepository.generateTasks(prompt.trim())
                
                // Save locally (instant UI updates offline-first)
                val listId = taskRepository.createTaskList(generated.title)
                for (task in generated.tasks) {
                    taskRepository.createTask(listId, task.title, task.order)
                }

                cyclerJob.cancel()
                _uiState.value = AiUiState.Success
            } catch (e: Exception) {
                cyclerJob.cancel()
                _uiState.value = AiUiState.Error(
                    "ИИ не смог распознать запрос. Перефразируйте его"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = AiUiState.Idle
    }
}
