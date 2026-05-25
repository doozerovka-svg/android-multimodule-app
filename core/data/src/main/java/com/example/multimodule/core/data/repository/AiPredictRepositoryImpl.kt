package com.example.multimodule.core.data.repository

import com.example.multimodule.core.domain.model.AiGeneratedList
import com.example.multimodule.core.domain.model.AiGeneratedTask
import com.example.multimodule.core.domain.repository.AiPredictRepository
import com.example.multimodule.core.network.ApiService
import com.example.multimodule.core.network.model.AiGenerateRequestDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiPredictRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AiPredictRepository {

    override suspend fun generateTasks(prompt: String): AiGeneratedList {
        val request = AiGenerateRequestDto(prompt = prompt)
        val response = apiService.generateTaskListWithAi(request)
        
        return AiGeneratedList(
            title = response.title,
            tasks = response.tasks.map { dto ->
                AiGeneratedTask(
                    title = dto.title,
                    order = dto.order
                )
            }
        )
    }
}
