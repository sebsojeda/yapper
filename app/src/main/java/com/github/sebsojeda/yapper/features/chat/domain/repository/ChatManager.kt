package com.github.sebsojeda.yapper.features.chat.domain.repository

import com.github.sebsojeda.yapper.features.chat.data.dto.CreateConversationDto
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateMessageDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetConversationDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetMessageDto
import kotlinx.coroutines.flow.Flow

interface ChatManager {
    suspend fun getConversations(): List<GetConversationDto>

    suspend fun createConversation(conversation: CreateConversationDto): GetConversationDto

    suspend fun deleteConversation(conversationId: String)

    suspend fun getMessages(conversationId: String): List<GetMessageDto>

    fun listenToMessages(conversationId: String): Flow<GetMessageDto>

    suspend fun createMessage(message: CreateMessageDto): GetMessageDto
}