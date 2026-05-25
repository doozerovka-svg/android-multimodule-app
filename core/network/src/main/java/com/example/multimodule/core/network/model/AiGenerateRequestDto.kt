package com.example.multimodule.core.network.model

import com.google.gson.annotations.SerializedName

data class AiGenerateRequestDto(
    @SerializedName("prompt")
    val prompt: String
)
