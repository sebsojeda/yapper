package com.github.sebsojeda.yapper.core.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.github.sebsojeda.yapper.core.AuthContext
import com.github.sebsojeda.yapper.core.LocalAuthContext
import com.github.sebsojeda.yapper.features.authentication.presentation.AuthenticationRoutes
import com.github.sebsojeda.yapper.features.authentication.presentation.authGraph
import com.github.sebsojeda.yapper.features.chat.presentation.chatGraph
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.postGraph

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    val user by viewModel.currentUser.collectAsState()

    if (!isAuthenticated) {
        NavHost(
            navController = navController,
            startDestination = AuthenticationRoutes.Auth.route,
            modifier = Modifier,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            authGraph(navController)
        }
    } else {
        CompositionLocalProvider(LocalAuthContext provides AuthContext(user!!, viewModel::signOut)) {
            NavHost(
                navController = navController,
                startDestination = PostRoutes.Post.route,
                modifier = Modifier,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                postGraph(navController)
                chatGraph(navController)
            }
        }
    }
}
