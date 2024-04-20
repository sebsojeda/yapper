package com.github.sebsojeda.yapper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.authentication.presentation.AuthenticationDestination
import com.github.sebsojeda.yapper.features.authentication.presentation.landing.LandingScreen
import com.github.sebsojeda.yapper.features.authentication.presentation.sign_in.SignInScreen
import com.github.sebsojeda.yapper.features.authentication.presentation.sign_up.SignUpScreen
import com.github.sebsojeda.yapper.features.authentication.presentation.sign_up_confirmation.SignUpConfirmationScreen
import com.github.sebsojeda.yapper.features.post.presentation.PostDestination
import com.github.sebsojeda.yapper.features.post.presentation.post_create.PostCreateScreen
import com.github.sebsojeda.yapper.features.post.presentation.post_detail.PostDetailScreen
import com.github.sebsojeda.yapper.features.post.presentation.post_list.PostsListScreen
import com.github.sebsojeda.yapper.ui.theme.YapperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YapperTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    Scaffold { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = AuthenticationDestination.Landing.route,
                            modifier = Modifier.padding(innerPadding),
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            composable(route = AuthenticationDestination.Landing.route) {
                                LandingScreen(navController)
                            }
                            composable(route = AuthenticationDestination.SignUp.route) {
                                SignUpScreen(navController)
                            }
                            composable(route = AuthenticationDestination.SignUpConfirmation.route + "/{${Constants.PARAM_EMAIL}}") {
                                SignUpConfirmationScreen(navController)
                            }
                            composable(route = AuthenticationDestination.SignIn.route) {
                                SignInScreen(navController)
                            }
                            composable(route = PostDestination.PostList.route) {
                                PostsListScreen(navController)
                            }
                            composable(
                                route = PostDestination.PostDetail.route + "/{${Constants.PARAM_POST_ID}}",
                                enterTransition = {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(500)
                                    )
                                },
                                exitTransition = {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                        animationSpec = tween(500)
                                    )
                                }
                            ) {
                                PostDetailScreen(navController)
                            }
                            composable(
                                route = PostDestination.PostCreate.route,
                                enterTransition = {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                        animationSpec = tween(500)
                                    )
                                },
                                exitTransition = {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                        animationSpec = tween(500)
                                    )
                                }
                            ) {
                                PostCreateScreen(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
