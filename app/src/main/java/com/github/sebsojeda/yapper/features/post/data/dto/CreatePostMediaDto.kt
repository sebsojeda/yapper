package com.github.sebsojeda.yapper.features.post.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePostMediaDto(
    @SerialName("post_id") val postId: String,
    @SerialName("media_id") val mediaId: String,
)
