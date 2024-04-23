package com.github.sebsojeda.yapper.features.post.domain.model

import com.github.sebsojeda.yapper.features.user.domain.model.User

data class Comment(
    val id: String,
    val userId: String,
    val user: User,
    val postId: String?,
    val content: String,
    val likes: Int,
    val comments: Int,
    val createdAt: String,
    val likedByUser: Boolean,
    val postMedia: List<PostMedia>
)
