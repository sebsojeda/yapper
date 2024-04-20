package com.github.sebsojeda.yapper.features.conversation.data.repository

import com.github.sebsojeda.yapper.features.conversation.data.datasource.ConversationMessageRemoteDataSource
import com.github.sebsojeda.yapper.features.conversation.data.dto.CreateConversationMessageDto
import com.github.sebsojeda.yapper.features.conversation.data.dto.GetConversationMessageDto
import com.github.sebsojeda.yapper.features.conversation.domain.repository.ConversationMessageRepository
import javax.inject.Inject

class ConversationMessageRepositoryImpl @Inject constructor(
    private val conversationMessageRemoteDataSource: ConversationMessageRemoteDataSource,
): ConversationMessageRepository {
    override suspend fun getConversationMessages(conversationId: String): List<GetConversationMessageDto> =
        conversationMessageRemoteDataSource.getConversationMessages(conversationId)

    override suspend fun createConversationMessage(message: CreateConversationMessageDto): GetConversationMessageDto =
        conversationMessageRemoteDataSource.createConversationMessage(message)
}
