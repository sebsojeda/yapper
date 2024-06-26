package com.github.sebsojeda.yapper.features.post.data.dto

import com.github.sebsojeda.yapper.features.post.domain.model.Post
import com.github.sebsojeda.yapper.features.user.data.dto.GetUserDto
import com.github.sebsojeda.yapper.features.user.data.dto.toUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPostDto(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("user") val user: GetUserDto,
    @SerialName("post_id") val postId: String?,
    @SerialName("post_reference") val postReference: GetPostReferenceDto?,
    @SerialName("content") val content: String,
    @SerialName("likes_count") val likesCount: Int,
    @SerialName("comments_count") val commentsCount: Int,
    @SerialName("created_at") val createdAt: String,
    @SerialName("likes") val likes: List<GetLikeDto>,
    @SerialName("post_media") val postMedia: List<GetPostMediaDto>,
)

fun GetPostDto.toPost() = Post(
    id = id,
    userId = userId,
    user = user.toUser(),
    postId = postId,
    postReference = postReference?.toPostReference(),
    content = content,
    likes = likesCount,
    comments = commentsCount,
    createdAt = createdAt,
    likedByUser = likes.isNotEmpty(),
    postMedia = postMedia.map { it.toPostMedia() }
)
