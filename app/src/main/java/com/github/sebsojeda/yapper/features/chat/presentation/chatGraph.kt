package com.github.sebsojeda.yapper.features.chat.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.github.sebsojeda.yapper.core.Constants

fun NavGraphBuilder.chatGraph(navController: NavHostController) {
    navigation(
        startDestination = ChatRoutes.ChatList.route,
        route = ChatRoutes.Chat.route
    ) {
        composable(
            route = ChatRoutes.ChatList.route
        ) {

        }
        composable(
            route = ChatRoutes.ChatDetail.route + "/{${Constants.PARAM_CONVERSATION_ID}}",
            arguments = listOf(
                navArgument(Constants.PARAM_CONVERSATION_ID) {
                    type = NavType.StringType
                }
            )
        ) {

        }
        composable(
            route = ChatRoutes.ChatCreate.route
        ) {

        }
    }
}