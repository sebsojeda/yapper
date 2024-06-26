package com.github.sebsojeda.yapper.features.post.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePostDto(
    @SerialName("content") val content: String,
)
