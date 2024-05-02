package com.github.sebsojeda.yapper.features.chat.presentation.chat_detail

import com.github.sebsojeda.yapper.features.chat.domain.model.Conversation

data class ChatDetailState(
    val conversationId: String = "",
    val focusReply: Boolean = false,
    val isLoading: Boolean = false,
    val conversation: Conversation? = null,
    val error: String = "",
)