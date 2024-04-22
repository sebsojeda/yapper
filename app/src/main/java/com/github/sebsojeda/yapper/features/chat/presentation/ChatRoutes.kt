package com.github.sebsojeda.yapper.features.chat.presentation

sealed class ChatRoutes(val route: String) {
    data object Chat : ChatRoutes("chat")
    data object ChatList : ChatRoutes("chat_list")
    data object ChatDetail : ChatRoutes("chat_detail")
    data object ChatCreate : ChatRoutes("chat_create")
}