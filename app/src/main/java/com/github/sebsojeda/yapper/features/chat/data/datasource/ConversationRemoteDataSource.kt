package com.github.sebsojeda.yapper.features.chat.data.datasource

import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateConversationDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetConversationDto
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConversationRemoteDataSource @Inject constructor(
    private val dataSource: Postgrest,
    private val auth: Auth,
){
    suspend fun getConversations(): List<GetConversationDto> =
        withContext(Dispatchers.IO) {
            val conversations = dataSource.from(Constants.TABLE_CONVERSATIONS)
                .select(Columns.raw("*, participants(*, user:user_id(*)), media:media_id(*), messages(*, user:user_id(*))")) {

                }
                .decodeList<GetConversationDto>()
            val conversationsWithUser = conversations.filter { conversation ->
                conversation.participants?.any { it.userId == auth.currentUserOrNull()?.id } ?: false
            }
            return@withContext conversationsWithUser.map { conversation ->
                conversation.copy(participants = conversation.participants?.filter { it.userId != auth.currentUserOrNull()?.id })
            }
        }

    suspend fun getConversation(conversationId: String): GetConversationDto =
        withContext(Dispatchers.IO) {
            val conversation = dataSource.from(Constants.TABLE_CONVERSATIONS)
                .select(Columns.raw("*, participants(*, user:user_id(*)), media:media_id(*), messages(*, user:user_id(*))")) {
                    filter { eq("id", conversationId) }
                }
                .decodeSingle<GetConversationDto>()
            return@withContext conversation.copy(participants = conversation.participants?.filter { it.userId != auth.currentUserOrNull()?.id })
        }

    suspend fun createConversation(conversation: CreateConversationDto): GetConversationDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_CONVERSATIONS)
                .insert(conversation) {
                    select(Columns.raw("*, participants(*, user:user_id(*)), media:media_id(*), messages(*, user:user_id(*))"))
                }.decodeSingle<GetConversationDto>()
        }

    suspend fun deleteConversation(conversationId: String): Unit =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_CONVERSATIONS)
                .delete { filter { eq("id", conversationId) } }
        }
}