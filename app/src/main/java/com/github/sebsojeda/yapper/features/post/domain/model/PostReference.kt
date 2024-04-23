package com.github.sebsojeda.yapper.features.post.domain.model

import com.github.sebsojeda.yapper.features.user.domain.model.User

data class PostReference(
    val id: String,
    val userId: String,
    val user: User,
    val content: String,
    val createdAt: String,
    val postMedia: List<PostMedia>
)
