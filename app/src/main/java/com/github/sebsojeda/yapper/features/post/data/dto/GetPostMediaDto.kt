package com.github.sebsojeda.yapper.features.post.data.dto

import com.github.sebsojeda.yapper.core.data.dto.GetMediaDto
import com.github.sebsojeda.yapper.core.data.dto.toMedia
import com.github.sebsojeda.yapper.features.post.domain.model.PostMedia
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPostMediaDto(
    @SerialName("post_id") val postId: String,
    @SerialName("media_id") val mediaId: String,
    @SerialName("media") val media: GetMediaDto,
)

fun GetPostMediaDto.toPostMedia() = PostMedia(
    postId = postId,
    mediaId = mediaId,
    media = media.toMedia(),
)
