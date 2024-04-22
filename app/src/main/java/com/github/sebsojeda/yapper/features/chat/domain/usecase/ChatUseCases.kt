package com.github.sebsojeda.yapper.features.chat.domain.usecase

data class ChatUseCases(
    val getConversations: GetConversations,
    val getConversation: GetConversation,
    val getMessages: GetMessages,
    val listenToMessages: ListenToMessages,
    val createMessage: CreateMessage,
    val createConversation: CreateConversation,
)
