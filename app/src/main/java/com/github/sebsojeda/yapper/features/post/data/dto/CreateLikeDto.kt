package com.github.sebsojeda.yapper.features.post.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateLikeDto(
    @SerialName("post_id") val postId: String,
)
