package com.github.sebsojeda.yapper.features.conversation.data.dto

import com.github.sebsojeda.yapper.features.conversation.domain.model.ConversationMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetConversationMessageDto(
    @SerialName("id") val id: String,
    @SerialName("conversation_id") val conversationId: String,
    @SerialName("content") val content: String,
    @SerialName("created_at") val createdAt: String,
)

fun GetConversationMessageDto.toConversationMessage() = ConversationMessage(
    id = id,
    conversationId = conversationId,
    content = content,
    createdAt = createdAt,
)