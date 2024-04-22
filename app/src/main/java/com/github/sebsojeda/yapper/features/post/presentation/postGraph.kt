package com.github.sebsojeda.yapper.features.post.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.post.presentation.post_create.PostCreateScreen
import com.github.sebsojeda.yapper.features.post.presentation.post_detail.PostDetailScreen
import com.github.sebsojeda.yapper.features.post.presentation.post_list.PostListScreen
import com.github.sebsojeda.yapper.features.post.presentation.post_search.PostSearchScreen

fun NavGraphBuilder.postGraph(navController: NavHostController) {
    navigation(
        startDestination = PostRoutes.PostList.route,
        route = PostRoutes.Post.route
    ) {
        composable(
            route = PostRoutes.PostList.route
        ) {
            PostListScreen(navController)
        }
        composable(
            route = PostRoutes.PostDetail.route + "/{${Constants.PARAM_POST_ID}}?${Constants.PARAM_FOCUS_REPLY}={${Constants.PARAM_FOCUS_REPLY}}",
            arguments = listOf(
                navArgument(Constants.PARAM_POST_ID) {
                    type = NavType.StringType
                },
                navArgument(Constants.PARAM_FOCUS_REPLY) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            PostDetailScreen(navController)
        }
        composable(
            route = PostRoutes.PostCreate.route
        ) {
            PostCreateScreen(navController)
        }
        composable(
            route = PostRoutes.PostSearch.route
        ) {
            PostSearchScreen(navController)
        }
    }
}