package com.github.sebsojeda.yapper.features.chat.data.dto

import com.github.sebsojeda.yapper.features.chat.domain.model.Message
import com.github.sebsojeda.yapper.features.user.data.dto.GetUserDto
import com.github.sebsojeda.yapper.features.user.data.dto.toUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMessageDto(
    @SerialName("id") val id: String,
    @SerialName("conversation_id") val conversationId: String,
    @SerialName("content") val content: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("user_id") val userId: String,
    @SerialName("user") val user: GetUserDto,
)

fun GetMessageDto.toMessage() = Message(
    id = id,
    conversationId = conversationId,
    content = content,
    createdAt = createdAt,
    userId = userId,
    user = user.toUser(),
)
