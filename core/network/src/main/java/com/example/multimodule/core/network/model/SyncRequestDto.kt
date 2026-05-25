package com.example.multimodule.core.network.model

import com.google.gson.annotations.SerializedName

data class SyncRequestDto(
    @SerializedName("pendingLists")
    val pendingLists: List<TaskListDto>,
    @SerializedName("pendingTasks")
    val pendingTasks: List<TaskDto>
)
