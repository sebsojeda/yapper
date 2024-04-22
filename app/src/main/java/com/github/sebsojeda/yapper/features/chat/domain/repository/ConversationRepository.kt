package com.github.sebsojeda.yapper.features.chat.domain.repository

import com.github.sebsojeda.yapper.features.chat.data.dto.CreateConversationDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetConversationDto

interface ConversationRepository {
    suspend fun getConversations(): List<GetConversationDto>

    suspend fun getConversation(conversationId: String): GetConversationDto

    suspend fun createConversation(conversation: CreateConversationDto): GetConversationDto

    suspend fun deleteConversation(conversationId: String)
}
