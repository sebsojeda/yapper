package com.github.sebsojeda.yapper.features.conversation.domain.model

data class ConversationMessage(
    val id: String,
    val conversationId: String,
    val content: String,
    val createdAt: String,
)
