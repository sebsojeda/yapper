package com.github.sebsojeda.yapper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.github.sebsojeda.yapper.core.AuthContext
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.LocalAuthContext
import com.github.sebsojeda.yapper.core.presentation.AuthViewModel
import com.github.sebsojeda.yapper.features.authentication.presentation.AuthenticationRoutes
import com.github.sebsojeda.yapper.features.authentication.presentation.sign_in.SignInScreen
import com.github.sebsojeda.yapper.features.authentication.presentation.sign_in.SignInState
import com.github.sebsojeda.yapper.features.authentication.presentation.sign_in.SignInViewModel
import com.github.sebsojeda.yapper.features.authentication.presentation.sign_up.SignUpScreen
import com.github.sebsojeda.yapper.features.authentication.presentation.sign_up.SignUpState
import com.github.sebsojeda.yapper.features.authentication.presentation.sign_up.SignUpViewModel
import com.github.sebsojeda.yapper.features.authentication.presentation.welcome.WelcomeScreen
import com.github.sebsojeda.yapper.features.chat.presentation.ChatRoutes
import com.github.sebsojeda.yapper.features.chat.presentation.chat_detail.ChatDetailScreen
import com.github.sebsojeda.yapper.features.chat.presentation.chat_detail.ChatDetailState
import com.github.sebsojeda.yapper.features.chat.presentation.chat_detail.ChatDetailViewModel
import com.github.sebsojeda.yapper.features.chat.presentation.chat_list.ChatListScreen
import com.github.sebsojeda.yapper.features.chat.presentation.chat_list.ChatListState
import com.github.sebsojeda.yapper.features.chat.presentation.chat_list.ChatListViewModel
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.post_detail.PostDetailScreen
import com.github.sebsojeda.yapper.features.post.presentation.post_detail.PostDetailState
import com.github.sebsojeda.yapper.features.post.presentation.post_detail.PostDetailViewModel
import com.github.sebsojeda.yapper.features.post.presentation.post_list.PostListScreen
import com.github.sebsojeda.yapper.features.post.presentation.post_list.PostListState
import com.github.sebsojeda.yapper.features.post.presentation.post_list.PostListViewModel
import com.github.sebsojeda.yapper.features.post.presentation.post_search.PostSearchScreen
import com.github.sebsojeda.yapper.features.post.presentation.post_search.PostSearchState
import com.github.sebsojeda.yapper.features.post.presentation.post_search.PostSearchViewModel
import com.github.sebsojeda.yapper.ui.theme.YapperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            YapperTheme {
                val authViewModel: AuthViewModel = hiltViewModel()
                val auth by authViewModel.state.collectAsState()
                if (!auth.isAuthenticated || auth.currentUser == null) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = AuthenticationRoutes.Auth.route,
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None }
                    ) {
                        navigation(
                            startDestination = AuthenticationRoutes.Welcome.route,
                            route = AuthenticationRoutes.Auth.route
                        ) {
                            composable(
                                route = AuthenticationRoutes.Welcome.route
                            ) {
                                WelcomeScreen(
                                    navigateTo = navController::navigate
                                )
                            }
                            composable(
                                route = AuthenticationRoutes.SignUp.route
                            ) {
                                val viewModel: SignUpViewModel = hiltViewModel()
                                val state: SignUpState by viewModel.state.collectAsState()
                                SignUpScreen(
                                    state = state,
                                    navigateTo = navController::navigate,
                                    signUp = viewModel::signUp
                                )
                            }
                            composable(
                                route = AuthenticationRoutes.SignIn.route
                            ) {
                                val viewModel: SignInViewModel = hiltViewModel()
                                val state: SignInState by viewModel.state.collectAsState()
                                SignInScreen(
                                    state = state,
                                    navigateTo = navController::navigate,
                                    signIn = viewModel::signIn
                                )
                            }
                        }
                    }
                } else {
                    val navController = rememberNavController()
                    CompositionLocalProvider(LocalAuthContext provides AuthContext(auth.currentUser!!, authViewModel::signOut)) {
                        NavHost(
                            navController = navController,
                            startDestination = PostRoutes.Post.route,
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            navigation(
                                startDestination = PostRoutes.PostList.route,
                                route = PostRoutes.Post.route
                            ) {
                                composable(
                                    route = PostRoutes.PostList.route
                                ) {
                                    val viewModel: PostListViewModel = hiltViewModel()
                                    val state: PostListState by viewModel.state.collectAsState()
                                    PostListScreen(
                                        state = state,
                                        currentRoute = it.destination.route,
                                        navigateTo = navController::navigate,
                                        toggleLike = viewModel::toggleLike,
                                        createPost = viewModel::createPost
                                    )
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
                                    val viewModel: PostDetailViewModel = hiltViewModel()
                                    val state: PostDetailState by viewModel.state.collectAsState()
                                    PostDetailScreen(
                                        state = state,
                                        currentRoute = it.destination.route,
                                        navigateTo = navController::navigate,
                                        navigateBack = { navController.navigateUp() },
                                        toggleLike = viewModel::toggleLike,
                                        toggleCommentLike = viewModel::toggleCommentLike,
                                        createComment = viewModel::createComment
                                    )
                                }
                                composable(
                                    route = PostRoutes.PostSearch.route
                                ) {
                                    val viewModel: PostSearchViewModel = hiltViewModel()
                                    val state: PostSearchState by viewModel.state.collectAsState()
                                    PostSearchScreen(
                                        state = state,
                                        currentRoute = it.destination.route,
                                        navigateTo = navController::navigate,
                                        toggleLike = viewModel::toggleLike,
                                        search = viewModel::search,
                                        createPost = viewModel::createPost
                                    )
                                }
                            }
                            navigation(
                                startDestination = ChatRoutes.ChatList.route,
                                route = ChatRoutes.Chat.route
                            ) {
                                composable(
                                    route = ChatRoutes.ChatList.route
                                ) {
                                    val viewModel: ChatListViewModel = hiltViewModel()
                                    val state: ChatListState by viewModel.state.collectAsState()
                                    ChatListScreen(
                                        state = state,
                                        currentRoute = it.destination.route,
                                        navigateTo = navController::navigate,
                                        createChat = viewModel::createChat
                                    )
                                }
                                composable(
                                    route = "${ChatRoutes.ChatDetail.route}/{${Constants.PARAM_CONVERSATION_ID}}",
                                    arguments = listOf(
                                        navArgument(Constants.PARAM_CONVERSATION_ID) {
                                            type = NavType.StringType
                                            nullable = false
                                        }
                                    )
                                ) {
                                    val viewModel: ChatDetailViewModel = hiltViewModel()
                                    val state: ChatDetailState by viewModel.state.collectAsState()
                                    ChatDetailScreen(
                                        state = state,
                                        currentRoute = it.destination.route,
                                        navigateTo = navController::navigate,
                                        navigateBack = { navController.navigateUp() },
                                        createMessage = viewModel::createMessage,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
