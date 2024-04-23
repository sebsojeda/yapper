package com.github.sebsojeda.yapper.features.post.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCommentDto(
    @SerialName("post_id") val postId: String?,
    @SerialName("content") val content: String,
)
