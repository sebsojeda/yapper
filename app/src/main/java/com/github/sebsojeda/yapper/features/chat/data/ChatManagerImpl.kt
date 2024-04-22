package com.github.sebsojeda.yapper.features.chat.data

import com.github.sebsojeda.yapper.features.chat.data.dto.CreateConversationDto
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateMessageDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetConversationDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetMessageDto
import com.github.sebsojeda.yapper.features.chat.domain.repository.ChatManager
import com.github.sebsojeda.yapper.features.chat.domain.repository.ConversationRepository
import com.github.sebsojeda.yapper.features.chat.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatManagerImpl @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
): ChatManager {
    override suspend fun getConversations(): List<GetConversationDto> =
        conversationRepository.getConversations()

    override suspend fun createConversation(conversation: CreateConversationDto): GetConversationDto =
        conversationRepository.createConversation(conversation)

    override suspend fun deleteConversation(conversationId: String): Unit =
        conversationRepository.deleteConversation(conversationId)

    override suspend fun getMessages(conversationId: String): List<GetMessageDto> =
        messageRepository.getMessages(conversationId)

    override fun listenToMessages(conversationId: String): Flow<GetMessageDto> =
        messageRepository.listenToMessages(conversationId)

    override suspend fun createMessage(message: CreateMessageDto): GetMessageDto =
        messageRepository.createMessage(message)
}