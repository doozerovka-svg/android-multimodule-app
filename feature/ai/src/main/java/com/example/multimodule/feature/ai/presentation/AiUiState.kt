package com.example.multimodule.feature.ai.presentation

sealed interface AiUiState {
    data object Idle : AiUiState
    data class Loading(val message: String) : AiUiState
    data object Success : AiUiState
    data class Error(val message: String) : AiUiState
}
