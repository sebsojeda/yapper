package com.github.sebsojeda.yapper.features.chat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateParticipantDto(
    @SerialName("conversation_id") val conversationId: String,
    @SerialName("user_id") val userId: String,
)