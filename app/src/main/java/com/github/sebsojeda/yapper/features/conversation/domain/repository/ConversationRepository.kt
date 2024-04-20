package com.github.sebsojeda.yapper.features.conversation.domain.repository

import com.github.sebsojeda.yapper.features.conversation.data.dto.CreateConversationDto
import com.github.sebsojeda.yapper.features.conversation.data.dto.GetConversationDto

interface ConversationRepository {
    suspend fun getConversations(senderId: String, receiverId: String): List<GetConversationDto>

    suspend fun createConversation(conversation: CreateConversationDto): GetConversationDto

    suspend fun deleteConversation(conversationId: String): Unit
}
