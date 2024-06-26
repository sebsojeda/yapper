package com.github.sebsojeda.yapper.features.post.data.dto

import com.github.sebsojeda.yapper.features.post.domain.model.Comment
import com.github.sebsojeda.yapper.features.user.data.dto.GetUserDto
import com.github.sebsojeda.yapper.features.user.data.dto.toUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCommentDto(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("user") val user: GetUserDto,
    @SerialName("post_id") val postId: String?,
    @SerialName("content") val content: String,
    @SerialName("likes_count") val likesCount: Int,
    @SerialName("comments_count") val commentsCount: Int,
    @SerialName("created_at") val createdAt: String,
    @SerialName("likes") val likes: List<GetLikeDto>,
    @SerialName("post_media") val postMedia: List<GetPostMediaDto>,
)

fun GetCommentDto.toComment() = Comment(
    id = id,
    userId = userId,
    user = user.toUser(),
    postId = postId,
    content = content,
    likes = likesCount,
    comments = commentsCount,
    createdAt = createdAt,
    likedByUser = likes.isNotEmpty(),
    postMedia = postMedia.map { it.toPostMedia() }
)
