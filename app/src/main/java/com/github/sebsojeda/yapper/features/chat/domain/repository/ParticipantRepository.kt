package com.github.sebsojeda.yapper.features.chat.domain.repository

import com.github.sebsojeda.yapper.features.chat.data.dto.CreateParticipantDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetParticipantDto

interface ParticipantRepository {
    suspend fun getParticipants(conversationId: String): List<GetParticipantDto>
    suspend fun createParticipant(participant: CreateParticipantDto): GetParticipantDto
}