package com.github.sebsojeda.yapper.features.chat.data.datasource

import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateMessageDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetMessageDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessageRemoteDataSource @Inject constructor(
    private val dataSource: Postgrest,
    private val realtime: Realtime
) {
    suspend fun getMessages(conversationId: String): List<GetMessageDto> =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_MESSAGES)
                .select(Columns.raw("*, user:user_id(*)")) {
                    filter {
                        eq("conversation_id", conversationId)
                    }
                    order("created_at", order = Order.ASCENDING)
                }
                .decodeList<GetMessageDto>()
        }

    fun listenToMessages(conversationId: String): Flow<GetMessageDto> {
        val channel = realtime.channel("messages:$conversationId")
        val channelFlow = channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
            table = Constants.TABLE_MESSAGES
        }

        return channelFlow.transform { action ->
                val record = action.decodeRecord<GetMessageDto>()
                val message = dataSource.from(Constants.TABLE_MESSAGES)
                    .select(Columns.raw("*, user:user_id(*)")) {
                        filter { eq("id", record.id) }
                    }
                    .decodeSingle<GetMessageDto>()
                emit(message)
            }
    }

    suspend fun createMessage(message: CreateMessageDto): GetMessageDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_MESSAGES)
                .insert(message) { select() }
                .decodeSingle<GetMessageDto>()
        }
}