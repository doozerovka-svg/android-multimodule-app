package com.example.multimodule.core.network.model

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("accessToken")
    val accessToken: String
)
