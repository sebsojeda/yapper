package com.github.sebsojeda.yapper.features.chat.data.repository

import com.github.sebsojeda.yapper.features.chat.data.datasource.MessageRemoteDataSource
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateMessageDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetMessageDto
import com.github.sebsojeda.yapper.features.chat.domain.repository.MessageRepository
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageRemoteDataSource: MessageRemoteDataSource,
): MessageRepository {
    override suspend fun getMessages(conversationId: String): List<GetMessageDto> =
        messageRemoteDataSource.getMessages(conversationId)

    override suspend fun listenToMessages(conversationId: String) =
        messageRemoteDataSource.listenToMessages(conversationId)

    override suspend fun createMessage(message: CreateMessageDto): GetMessageDto =
        messageRemoteDataSource.createMessage(message)
}
