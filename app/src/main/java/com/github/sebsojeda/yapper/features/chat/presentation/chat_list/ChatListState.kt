package com.github.sebsojeda.yapper.features.chat.presentation.chat_list

import com.github.sebsojeda.yapper.features.chat.domain.model.Conversation

data class ChatListState(
    val isLoading: Boolean = false,
    val isChatLoading: Boolean = false,
    val chat: Conversation? = null,
    val conversations: List<Conversation> = emptyList(),
    val error: String = ""
)
