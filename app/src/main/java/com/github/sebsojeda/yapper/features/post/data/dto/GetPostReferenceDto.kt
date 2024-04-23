package com.github.sebsojeda.yapper.features.post.data.dto

import com.github.sebsojeda.yapper.features.post.domain.model.PostReference
import com.github.sebsojeda.yapper.features.user.data.dto.GetUserDto
import com.github.sebsojeda.yapper.features.user.data.dto.toUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPostReferenceDto(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("user") val user: GetUserDto,
    @SerialName("content") val content: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("post_media") val postMedia: List<GetPostMediaDto>,
)

fun GetPostReferenceDto.toPostReference() = PostReference(
    id = id,
    userId = userId,
    user = user.toUser(),
    content = content,
    createdAt = createdAt,
    postMedia = postMedia.map { it.toPostMedia() }
)
