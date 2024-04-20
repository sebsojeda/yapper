package com.github.sebsojeda.yapper.features.conversation.data.datasource

import com.github.sebsojeda.yapper.features.conversation.data.dto.CreateConversationDto
import com.github.sebsojeda.yapper.features.conversation.data.dto.GetConversationDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConversationRemoteDataSource @Inject constructor(
    private val dataSource: Postgrest
){
    private companion object {
        const val TABLE = "conversations"
    }

    suspend fun getConversations(senderId: String, receiverId: String): List<GetConversationDto> =
        withContext(Dispatchers.IO) {
            dataSource.from(TABLE)
                .select(Columns.raw("*, conversation_messages:conversation_id(*), sender_user:sender_id(*), receiver_user:receiver_id(*)"), false) {
                    filter {
                        or {
                            eq("sender_id", senderId)
                            eq("receiver_id", receiverId)
                        }
                    }
                    order("created_at", order = Order.ASCENDING, referencedTable = "conversation_messages")
                    limit(1, referencedTable = "conversation_messages")
                }
                .decodeList<GetConversationDto>()
        }

    suspend fun createConversation(conversation: CreateConversationDto): GetConversationDto =
        withContext(Dispatchers.IO) {
            dataSource.from(TABLE)
                .insert(conversation) {
                    select(Columns.raw("*, sender_user:sender_id(*), receiver_user:receiver_id(*)"))
                }.decodeSingle<GetConversationDto>()
        }

    suspend fun deleteConversation(conversationId: String): Unit =
        withContext(Dispatchers.IO) {
            dataSource.from(TABLE)
                .delete { filter { eq("id", conversationId) } }
        }
}