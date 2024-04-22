package com.github.sebsojeda.yapper.features.chat.data.repository

import com.github.sebsojeda.yapper.features.chat.data.datasource.ParticipantRemoteDataSource
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateParticipantDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetParticipantDto
import com.github.sebsojeda.yapper.features.chat.domain.repository.ParticipantRepository
import javax.inject.Inject

class ParticipantRepositoryImpl @Inject constructor(
    private val participantRemoteDataSource: ParticipantRemoteDataSource,
) : ParticipantRepository {
    override suspend fun getParticipants(conversationId: String): List<GetParticipantDto> =
        participantRemoteDataSource.getParticipants(conversationId)

    override suspend fun createParticipant(participant: CreateParticipantDto): GetParticipantDto =
        participantRemoteDataSource.createParticipant(participant)
}
