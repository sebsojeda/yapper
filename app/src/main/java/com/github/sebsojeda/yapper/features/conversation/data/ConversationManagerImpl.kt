package com.github.sebsojeda.yapper.features.conversation.data

import com.github.sebsojeda.yapper.features.conversation.data.dto.CreateConversationDto
import com.github.sebsojeda.yapper.features.conversation.data.dto.CreateConversationMessageDto
import com.github.sebsojeda.yapper.features.conversation.data.dto.GetConversationDto
import com.github.sebsojeda.yapper.features.conversation.data.dto.GetConversationMessageDto
import com.github.sebsojeda.yapper.features.conversation.domain.repository.ConversationManager
import com.github.sebsojeda.yapper.features.conversation.domain.repository.ConversationMessageRepository
import com.github.sebsojeda.yapper.features.conversation.domain.repository.ConversationRepository
import javax.inject.Inject

class ConversationManagerImpl @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val conversationMessageRepository: ConversationMessageRepository,
): ConversationManager {
    override suspend fun getConversations(userId: String): List<GetConversationDto> =
        conversationRepository.getConversations(userId, userId)

    override suspend fun createConversation(conversation: CreateConversationDto): GetConversationDto =
        conversationRepository.createConversation(conversation)

    override suspend fun deleteConversation(conversationId: String): Unit =
        conversationRepository.deleteConversation(conversationId)

    override suspend fun getConversationMessages(conversationId: String): List<GetConversationMessageDto> =
        conversationMessageRepository.getConversationMessages(conversationId)

    override suspend fun createConversationMessage(message: CreateConversationMessageDto): GetConversationMessageDto =
        conversationMessageRepository.createConversationMessage(message)
}