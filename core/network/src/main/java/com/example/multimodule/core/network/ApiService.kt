package com.example.multimodule.core.network

import com.example.multimodule.core.network.model.AiGenerateRequestDto
import com.example.multimodule.core.network.model.AiGenerateResponseDto
import com.example.multimodule.core.network.model.AuthResponseDto
import com.example.multimodule.core.network.model.SyncRequestDto
import com.example.multimodule.core.network.model.SyncResponseDto
import com.example.multimodule.core.network.model.TaskDto
import com.example.multimodule.core.network.model.TaskListDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: Map<String, String>
    ): AuthResponseDto

    @POST("auth/register")
    suspend fun register(
        @Body request: Map<String, String>
    ): AuthResponseDto

    @GET("lists")
    suspend fun getTaskLists(): List<TaskListDto>

    @GET("lists/{listId}/tasks")
    suspend fun getTasksForList(
        @Path("listId") listId: String
    ): List<TaskDto>

    @POST("api/v1/sync/batch")
    suspend fun batchSync(
        @Body syncRequest: SyncRequestDto
    ): SyncResponseDto

    @POST("api/v1/ai/generate")
    suspend fun generateTaskListWithAi(
        @Body request: AiGenerateRequestDto
    ): AiGenerateResponseDto
}
