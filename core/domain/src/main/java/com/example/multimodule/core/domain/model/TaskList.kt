package com.example.multimodule.core.domain.model

data class TaskList(
    val id: String,
    val userId: String,
    val title: String,
    val createdAt: Long,
    val syncStatus: SyncStatus
)
