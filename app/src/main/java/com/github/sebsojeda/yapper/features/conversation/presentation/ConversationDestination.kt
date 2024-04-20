package com.github.sebsojeda.yapper.features.conversation.presentation

sealed class ConversationDestination(val route: String) {
    data object ConversationList : ConversationDestination("conversation_list")
    data object ConversationDetail : ConversationDestination("conversation_detail")
}