package com.github.sebsojeda.yapper.features.chat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMessageDto(
    @SerialName("conversation_id") val conversationId: String,
    @SerialName("content") val content: String,
)
