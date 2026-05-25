package com.example.multimodule.core.network.model

import com.google.gson.annotations.SerializedName

data class TaskListDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("createdAt")
    val createdAt: Long
)
