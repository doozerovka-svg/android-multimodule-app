package com.example.multimodule.core.network.model

import com.google.gson.annotations.SerializedName

data class AiGenerateResponseDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("tasks")
    val tasks: List<AiGeneratedTaskDto>
)

data class AiGeneratedTaskDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("order")
    val order: Int
)
