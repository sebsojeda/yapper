package com.github.sebsojeda.yapper.features.chat.data.datasource

import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateConversationDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetConversationDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConversationRemoteDataSource @Inject constructor(
    private val dataSource: Postgrest
){
    suspend fun getConversations(): List<GetConversationDto> =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_CONVERSATIONS)
                .select(Columns.raw("*, media:media_id(*)")) {
                    order("created_at", order = Order.ASCENDING)
                }
                .decodeList<GetConversationDto>()
        }

    suspend fun createConversation(conversation: CreateConversationDto): GetConversationDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_CONVERSATIONS)
                .insert(conversation) {
                    select(Columns.raw("*, media:media_id(*)"))
                }.decodeSingle<GetConversationDto>()
        }

    suspend fun deleteConversation(conversationId: String): Unit =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_CONVERSATIONS)
                .delete { filter { eq("id", conversationId) } }
        }
}