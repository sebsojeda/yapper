package com.github.sebsojeda.yapper.features.chat.presentation.chat_create

data class ChatCreateState(
    val isLoading: Boolean = false,
    val isChatCreated: Boolean = false,
    val conversationId: String? = null,
    val error: String = "",
)
