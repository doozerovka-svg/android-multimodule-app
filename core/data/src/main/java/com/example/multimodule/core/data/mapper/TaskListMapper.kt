package com.example.multimodule.core.data.mapper

import com.example.multimodule.core.database.entity.TaskListEntity
import com.example.multimodule.core.domain.model.SyncStatus
import com.example.multimodule.core.domain.model.TaskList
import com.example.multimodule.core.network.model.TaskListDto

fun TaskListEntity.toDomain(): TaskList {
    return TaskList(
        id = id,
        userId = userId,
        title = title,
        createdAt = createdAt,
        syncStatus = try {
            SyncStatus.valueOf(syncStatus)
        } catch (e: Exception) {
            SyncStatus.PENDING
        }
    )
}

fun TaskList.toEntity(): TaskListEntity {
    return TaskListEntity(
        id = id,
        userId = userId,
        title = title,
        createdAt = createdAt,
        syncStatus = syncStatus.name
    )
}

fun TaskListDto.toEntity(syncStatus: SyncStatus): TaskListEntity {
    return TaskListEntity(
        id = id,
        userId = userId,
        title = title,
        createdAt = createdAt,
        syncStatus = syncStatus.name
    )
}

fun TaskListEntity.toDto(): TaskListDto {
    return TaskListDto(
        id = id,
        userId = userId,
        title = title,
        createdAt = createdAt
    )
}
