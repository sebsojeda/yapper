package com.github.sebsojeda.yapper.features.conversation.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateConversationMessageDto(
    @SerialName("conversation_id") val conversationId: String,
    @SerialName("content") val content: String,
)
