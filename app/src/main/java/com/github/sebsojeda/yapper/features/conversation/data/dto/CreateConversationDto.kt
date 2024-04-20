package com.github.sebsojeda.yapper.features.conversation.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateConversationDto(
    @SerialName("sender_id") val senderId: String,
    @SerialName("receiver_id") val receiverId: String,
)
