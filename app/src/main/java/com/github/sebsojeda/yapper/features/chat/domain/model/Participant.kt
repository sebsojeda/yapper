package com.github.sebsojeda.yapper.features.chat.domain.model

import com.github.sebsojeda.yapper.features.user.domain.model.User

data class Participant(
    val conversationId: String,
    val userId: String,
    val user: User,
    val createdAt: String,
)
