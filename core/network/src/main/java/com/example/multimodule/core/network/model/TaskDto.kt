package com.example.multimodule.core.network.model

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("listId")
    val listId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("isCompleted")
    val isCompleted: Boolean,
    @SerializedName("order")
    val order: Int
)
