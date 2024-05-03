package com.github.sebsojeda.yapper.features.chat.data

import com.github.sebsojeda.yapper.core.domain.repository.MediaRepository
import com.github.sebsojeda.yapper.core.domain.repository.MediaStorageRepository
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateConversationDto
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateMessageDto
import com.github.sebsojeda.yapper.features.chat.data.dto.CreateParticipantDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetConversationDto
import com.github.sebsojeda.yapper.features.chat.data.dto.GetMessageDto
import com.github.sebsojeda.yapper.features.chat.domain.repository.ChatManager
import com.github.sebsojeda.yapper.features.chat.domain.repository.ConversationRepository
import com.github.sebsojeda.yapper.features.chat.domain.repository.MessageRepository
import com.github.sebsojeda.yapper.features.chat.domain.repository.ParticipantRepository
import com.github.sebsojeda.yapper.features.user.data.dto.GetUserDto
import com.github.sebsojeda.yapper.features.user.domain.repository.UserRepository
import io.github.jan.supabase.gotrue.Auth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatManagerImpl @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val participantRepository: ParticipantRepository,
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val mediaStorageRepository: MediaStorageRepository,
    private val mediaRepository: MediaRepository,
    private val auth: Auth,
): ChatManager {
    override suspend fun getConversations(): List<GetConversationDto> =
        conversationRepository.getConversations()

    override suspend fun getConversation(conversationId: String): GetConversationDto =
        conversationRepository.getConversation(conversationId)

    override suspend fun createConversation(participants: List<String>, content: String): GetConversationDto {
        val users = mutableListOf<GetUserDto>()
        participants.forEach { username ->
            try {
                val user = userRepository.getUserByUsername(username)
                if (user.id != auth.currentUserOrNull()!!.id) {
                    users.add(user)
                }
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("User @$username not found")
            }
        }

        if (users.isEmpty()) {
            throw IllegalArgumentException("No participants provided")
        }

        val conversation = conversationRepository.createConversation(CreateConversationDto(null, null))
        participantRepository.createParticipant(CreateParticipantDto(conversation.id, auth.currentUserOrNull()!!.id))
        users.forEach { user ->
            participantRepository.createParticipant(CreateParticipantDto(conversation.id, user.id))
        }

        val message = messageRepository.createMessage(CreateMessageDto(conversation.id, content))

        return conversation.copy(messages = listOf(message))
    }

    override suspend fun deleteConversation(conversationId: String): Unit =
        conversationRepository.deleteConversation(conversationId)

    override suspend fun getMessages(conversationId: String): List<GetMessageDto> =
        messageRepository.getMessages(conversationId)

    override suspend fun listenToMessages(conversationId: String): Flow<GetMessageDto> =
        messageRepository.listenToMessages(conversationId)

    override suspend fun createMessage(message: CreateMessageDto): GetMessageDto =
        messageRepository.createMessage(message)
}