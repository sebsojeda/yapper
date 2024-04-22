package com.github.sebsojeda.yapper.features.chat.data.datasource

import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateParticipantDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetParticipantDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ParticipantRemoteDataSource @Inject constructor(
    private val dataSource: Postgrest
){
    suspend fun getParticipants(conversationId: String): List<GetParticipantDto> =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_PARTICIPANTS)
                .select(Columns.raw("*, user:user_id(*)")) {
                    filter { eq("conversation_id", conversationId) }
                }
                .decodeList<GetParticipantDto>()
        }

    suspend fun createParticipant(participant: CreateParticipantDto): GetParticipantDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_PARTICIPANTS)
                .insert(participant) {
                    select(Columns.raw("*, user:user_id(*)"))
                }.decodeSingle<GetParticipantDto>()
        }
}