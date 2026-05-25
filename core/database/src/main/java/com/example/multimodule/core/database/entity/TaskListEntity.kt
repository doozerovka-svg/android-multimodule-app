package com.example.multimodule.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_lists")
data class TaskListEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val title: String,
    val createdAt: Long,
    val syncStatus: String // PENDING, COMPLETED
)
