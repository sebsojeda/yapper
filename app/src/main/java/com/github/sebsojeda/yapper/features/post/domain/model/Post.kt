package com.github.sebsojeda.yapper.features.post.domain.model

import com.github.sebsojeda.yapper.features.user.domain.model.User

data class Post(
    val id: String,
    val userId: String,
    val user: User,
    val postId: String?,
    val content: String,
    val likes: Int,
    val comments: Int,
    val createdAt: String,
    val postMedia: List<PostMedia>
)