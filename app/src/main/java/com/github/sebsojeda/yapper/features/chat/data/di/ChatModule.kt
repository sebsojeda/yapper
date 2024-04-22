package com.github.sebsojeda.yapper.features.chat.data.di

import com.github.sebsojeda.yapper.core.domain.repository.MediaRepository
import com.github.sebsojeda.yapper.core.domain.repository.MediaStorageRepository
import com.github.sebsojeda.yapper.features.chat.data.ChatManagerImpl
import com.github.sebsojeda.yapper.features.chat.data.datasource.ConversationRemoteDataSource
import com.github.sebsojeda.yapper.features.chat.data.datasource.MessageRemoteDataSource
import com.github.sebsojeda.yapper.features.chat.data.datasource.ParticipantRemoteDataSource
import com.github.sebsojeda.yapper.features.chat.data.repository.ConversationRepositoryImpl
import com.github.sebsojeda.yapper.features.chat.data.repository.MessageRepositoryImpl
import com.github.sebsojeda.yapper.features.chat.data.repository.ParticipantRepositoryImpl
import com.github.sebsojeda.yapper.features.chat.domain.repository.ChatManager
import com.github.sebsojeda.yapper.features.chat.domain.repository.ConversationRepository
import com.github.sebsojeda.yapper.features.chat.domain.repository.MessageRepository
import com.github.sebsojeda.yapper.features.chat.domain.repository.ParticipantRepository
import com.github.sebsojeda.yapper.features.chat.domain.usecase.ChatUseCases
import com.github.sebsojeda.yapper.features.chat.domain.usecase.CreateConversation
import com.github.sebsojeda.yapper.features.chat.domain.usecase.CreateMessage
import com.github.sebsojeda.yapper.features.chat.domain.usecase.GetConversation
import com.github.sebsojeda.yapper.features.chat.domain.usecase.GetConversations
import com.github.sebsojeda.yapper.features.chat.domain.usecase.GetMessages
import com.github.sebsojeda.yapper.features.chat.domain.usecase.ListenToMessages
import com.github.sebsojeda.yapper.features.user.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.gotrue.Auth
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ChatModule {
    @Provides
    @Singleton
    fun provideConversationRepository(conversationRemoteDataSource: ConversationRemoteDataSource): ConversationRepository {
        return ConversationRepositoryImpl(conversationRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(messageRemoteDataSource: MessageRemoteDataSource): MessageRepository {
        return MessageRepositoryImpl(messageRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideParticipantRepository(participantRemoteDataSource: ParticipantRemoteDataSource): ParticipantRepository {
        return ParticipantRepositoryImpl(participantRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideChatManager(
        conversationRepository: ConversationRepository,
        participantRepository: ParticipantRepository,
        userRepository: UserRepository,
        messageRepository: MessageRepository,
        mediaRepository: MediaRepository,
        mediaStorageRepository: MediaStorageRepository,
        auth: Auth
    ): ChatManager {
        return ChatManagerImpl(
            conversationRepository,
            participantRepository,
            userRepository,
            messageRepository,
            mediaStorageRepository,
            mediaRepository,
            auth
        )
    }

    @Provides
    @Singleton
    fun provideChatUseCases(chatManager: ChatManager): ChatUseCases {
        return ChatUseCases(
            getConversations = GetConversations(chatManager),
            getConversation = GetConversation(chatManager),
            getMessages = GetMessages(chatManager),
            listenToMessages = ListenToMessages(chatManager),
            createMessage = CreateMessage(chatManager),
            createConversation = CreateConversation(chatManager),
        )
    }
}