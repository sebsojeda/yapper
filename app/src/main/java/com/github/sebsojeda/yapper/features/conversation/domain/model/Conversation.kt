package com.github.sebsojeda.yapper.features.conversation.domain.model

import com.github.sebsojeda.yapper.features.user.domain.model.User

data class Conversation(
    val id: String,
    val senderId: String,
    val sender: User,
    val receiverId: String,
    val receiver: User,
    val createdAt: String,
    val messages: List<ConversationMessage>,
)
