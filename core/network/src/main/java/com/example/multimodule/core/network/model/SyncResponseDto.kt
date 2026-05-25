package com.example.multimodule.core.network.model

import com.google.gson.annotations.SerializedName

data class SyncResponseDto(
    @SerializedName("syncedLists")
    val syncedLists: List<TaskListDto>,
    @SerializedName("syncedTasks")
    val syncedTasks: List<TaskDto>
)
