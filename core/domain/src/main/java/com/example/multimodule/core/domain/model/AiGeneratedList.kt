package com.example.multimodule.core.domain.model

data class AiGeneratedList(
    val title: String,
    val tasks: List<AiGeneratedTask>
)

data class AiGeneratedTask(
    val title: String,
    val order: Int
)
