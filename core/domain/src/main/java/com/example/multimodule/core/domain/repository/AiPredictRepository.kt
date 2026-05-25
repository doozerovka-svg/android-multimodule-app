package com.example.multimodule.core.domain.repository

import com.example.multimodule.core.domain.model.AiGeneratedList

interface AiPredictRepository {
    suspend fun generateTasks(prompt: String): AiGeneratedList
}
