package com.github.sebsojeda.yapper.features.conversation.data.datasource

import com.github.sebsojeda.yapper.features.conversation.data.dto.CreateConversationMessageDto
import com.github.sebsojeda.yapper.features.conversation.data.dto.GetConversationMessageDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConversationMessageRemoteDataSource @Inject constructor(
    private val dataSource: Postgrest,
    private val ioDispatcher: CoroutineDispatcher,
) {
    private companion object {
        const val TABLE = "conversation_messages"
    }

    suspend fun getConversationMessages(conversationId: String): List<GetConversationMessageDto> =
        withContext(ioDispatcher) {
            dataSource.from(TABLE)
                .select {
                    filter {
                        eq("conversation_id", conversationId)
                    }
                    order("created_at", order = Order.ASCENDING)
                }
                .decodeList<GetConversationMessageDto>()
        }

    suspend fun createConversationMessage(message: CreateConversationMessageDto): GetConversationMessageDto =
        withContext(ioDispatcher) {
            dataSource.from(TABLE)
                .insert(message) { select() }
                .decodeSingle<GetConversationMessageDto>()
        }
}