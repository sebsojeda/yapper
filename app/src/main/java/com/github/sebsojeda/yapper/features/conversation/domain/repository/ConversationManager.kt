package com.github.sebsojeda.yapper.features.conversation.domain.repository

import com.github.sebsojeda.yapper.features.conversation.data.dto.CreateConversationDto
import com.github.sebsojeda.yapper.features.conversation.data.dto.CreateConversationMessageDto
import com.github.sebsojeda.yapper.features.conversation.data.dto.GetConversationDto
import com.github.sebsojeda.yapper.features.conversation.data.dto.GetConversationMessageDto

interface ConversationManager {
    suspend fun getConversations(userId: String): List<GetConversationDto>

    suspend fun createConversation(conversation: CreateConversationDto): GetConversationDto

    suspend fun deleteConversation(conversationId: String): Unit

    suspend fun getConversationMessages(conversationId: String): List<GetConversationMessageDto>

    suspend fun createConversationMessage(message: CreateConversationMessageDto): GetConversationMessageDto
}