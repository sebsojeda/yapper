package com.github.sebsojeda.yapper.features.conversation.data.dto

import com.github.sebsojeda.yapper.features.conversation.domain.model.Conversation
import com.github.sebsojeda.yapper.features.user.data.dto.GetUserDto
import com.github.sebsojeda.yapper.features.user.data.dto.toUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetConversationDto(
    @SerialName("id") val id: String,
    @SerialName("sender_id") val senderId: String,
    @SerialName("sender_user") val senderUser: GetUserDto,
    @SerialName("receiver_id") val receiverId: String,
    @SerialName("receiver_user") val receiverUser: GetUserDto,
    @SerialName("created_at") val createdAt: String,
    @SerialName("conversation_messages") val conversationMessages: List<GetConversationMessageDto>
)

fun GetConversationDto.toConversation() = Conversation(
    id = id,
    senderId = senderId,
    sender = senderUser.toUser(),
    receiverId = receiverId,
    receiver = receiverUser.toUser(),
    createdAt = createdAt,
    messages = conversationMessages.map { it.toConversationMessage() }
)
