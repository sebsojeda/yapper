package com.github.sebsojeda.yapper.features.chat.data.dto

import com.github.sebsojeda.yapper.features.chat.domain.model.Participant
import com.github.sebsojeda.yapper.features.user.data.dto.GetUserDto
import com.github.sebsojeda.yapper.features.user.data.dto.toUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetParticipantDto(
    @SerialName("conversation_id") val conversationId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("user") val user: GetUserDto,
    @SerialName("created_at") val createdAt: String,
)

fun GetParticipantDto.toParticipant() = Participant(
    conversationId = conversationId,
    userId = userId,
    user = user.toUser(),
    createdAt = createdAt,
)
