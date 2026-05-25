package com.example.multimodule.core.domain.model

data class Task(
    val id: String,
    val listId: String,
    val title: String,
    val isCompleted: Boolean,
    val order: Int,
    val syncStatus: SyncStatus
)
