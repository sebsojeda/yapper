package com.github.sebsojeda.yapper.features.chat.data.repository

import com.github.sebsojeda.yapper.features.chat.data.datasource.ConversationRemoteDataSource
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateConversationDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetConversationDto
import com.github.sebsojeda.yapper.features.chat.domain.repository.ConversationRepository
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val conversationRemoteDataSource: ConversationRemoteDataSource,
): ConversationRepository {
    override suspend fun getConversations(): List<GetConversationDto> =
        conversationRemoteDataSource.getConversations()

    override suspend fun createConversation(conversation: CreateConversationDto): GetConversationDto =
        conversationRemoteDataSource.createConversation(conversation)

    override suspend fun deleteConversation(conversationId: String): Unit =
        conversationRemoteDataSource.deleteConversation(conversationId)
}