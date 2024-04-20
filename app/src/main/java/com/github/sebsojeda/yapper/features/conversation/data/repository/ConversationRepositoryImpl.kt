package com.github.sebsojeda.yapper.features.conversation.data.repository

import com.github.sebsojeda.yapper.features.conversation.data.datasource.ConversationRemoteDataSource
import com.github.sebsojeda.yapper.features.conversation.data.dto.CreateConversationDto
import com.github.sebsojeda.yapper.features.conversation.data.dto.GetConversationDto
import com.github.sebsojeda.yapper.features.conversation.domain.repository.ConversationRepository
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val conversationRemoteDataSource: ConversationRemoteDataSource,
): ConversationRepository {
    override suspend fun getConversations(senderId: String, receiverId: String): List<GetConversationDto> =
        conversationRemoteDataSource.getConversations(senderId, receiverId)

    override suspend fun createConversation(conversation: CreateConversationDto): GetConversationDto =
        conversationRemoteDataSource.createConversation(conversation)

    override suspend fun deleteConversation(conversationId: String): Unit =
        conversationRemoteDataSource.deleteConversation(conversationId)
}