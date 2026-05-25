package com.example.multimodule.core.data.mapper

import com.example.multimodule.core.database.entity.TaskEntity
import com.example.multimodule.core.domain.model.SyncStatus
import com.example.multimodule.core.domain.model.Task
import com.example.multimodule.core.network.model.TaskDto

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        listId = listId,
        title = title,
        isCompleted = isCompleted,
        order = order,
        syncStatus = try {
            SyncStatus.valueOf(syncStatus)
        } catch (e: Exception) {
            SyncStatus.PENDING
        }
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        listId = listId,
        title = title,
        isCompleted = isCompleted,
        order = order,
        syncStatus = syncStatus.name
    )
}

fun TaskDto.toEntity(syncStatus: SyncStatus): TaskEntity {
    return TaskEntity(
        id = id,
        listId = listId,
        title = title,
        isCompleted = isCompleted,
        order = order,
        syncStatus = syncStatus.name
    )
}

fun TaskEntity.toDto(): TaskDto {
    return TaskDto(
        id = id,
        listId = listId,
        title = title,
        isCompleted = isCompleted,
        order = order
    )
}
