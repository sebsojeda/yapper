package com.github.sebsojeda.yapper.features.chat.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.chat.presentation.chat_create.ChatCreateScreen
import com.github.sebsojeda.yapper.features.chat.presentation.chat_detail.ChatDetailScreen
import com.github.sebsojeda.yapper.features.chat.presentation.chat_list.ChatListScreen

fun NavGraphBuilder.chatGraph(navController: NavHostController) {
    navigation(
        startDestination = ChatRoutes.ChatList.route,
        route = ChatRoutes.Chat.route
    ) {
        composable(
            route = ChatRoutes.ChatList.route
        ) {
            ChatListScreen(navController)
        }
        composable(
            route = ChatRoutes.ChatDetail.route + "/{${Constants.PARAM_CONVERSATION_ID}}",
            arguments = listOf(
                navArgument(Constants.PARAM_CONVERSATION_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            ChatDetailScreen(navController)
        }
        composable(
            route = ChatRoutes.ChatCreate.route
        ) {
            ChatCreateScreen(navController)
        }
    }
}