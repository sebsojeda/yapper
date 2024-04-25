package com.github.sebsojeda.yapper

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.github.sebsojeda.yapper.features.authentication.presentation.AuthenticationRoutes
import com.github.sebsojeda.yapper.features.authentication.presentation.authGraph
import com.github.sebsojeda.yapper.features.chat.presentation.chatGraph
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.postGraph

@Composable
fun Yapper(viewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val isAuthenticated = viewModel.isAuthenticated.collectAsState().value
    val startDestination = if (isAuthenticated) PostRoutes.Post.route else AuthenticationRoutes.Auth.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        authGraph(navController)
        postGraph(navController)
        chatGraph(navController)
    }
}