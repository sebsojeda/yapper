package com.github.sebsojeda.yapper.features.chat.domain.model

import com.github.sebsojeda.yapper.features.user.domain.model.User

data class Message(
    val id: String,
    val conversationId: String,
    val content: String,
    val createdAt: String,
    val userId: String,
    val user: User,
)
