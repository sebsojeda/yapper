package com.github.sebsojeda.yapper.features.conversation.domain.repository

import com.github.sebsojeda.yapper.features.conversation.data.dto.CreateConversationMessageDto
import com.github.sebsojeda.yapper.features.conversation.data.dto.GetConversationMessageDto

interface ConversationMessageRepository {
    suspend fun getConversationMessages(conversationId: String): List<GetConversationMessageDto>

    suspend fun createConversationMessage(message: CreateConversationMessageDto): GetConversationMessageDto
}