package com.github.sebsojeda.yapper.features.chat.presentation.chat_list

import com.github.sebsojeda.yapper.features.chat.domain.model.Conversation

data class ChatListState(
    val isLoading: Boolean = false,
    val conversations: List<Conversation> = emptyList(),
    val error: String = ""
)
