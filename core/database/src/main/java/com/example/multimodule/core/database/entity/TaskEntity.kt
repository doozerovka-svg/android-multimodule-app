package com.example.multimodule.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val listId: String,
    val title: String,
    val isCompleted: Boolean,
    val order: Int,
    val syncStatus: String // PENDING, COMPLETED
)
