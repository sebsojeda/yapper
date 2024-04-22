package com.github.sebsojeda.yapper.features.chat.domain.repository

import com.github.sebsojeda.yapper.features.chat.data.dto.CreateMessageDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetMessageDto
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessages(conversationId: String): List<GetMessageDto>

    fun listenToMessages(conversationId: String): Flow<GetMessageDto>

    suspend fun createMessage(message: CreateMessageDto): GetMessageDto
}