package com.github.sebsojeda.yapper.features.post.presentation.post_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.components.YapperLayout
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.components.PostListItem

@Composable
fun PostListScreen(
    navController: NavController,
    viewModel: PostListViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        if (currentRoute == PostRoutes.PostList.route) {
            viewModel.getPosts()
        }
    }

    YapperLayout (
        navController = navController,
        title = { Text(text = "Yapper") },
        modifier = Modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(PostRoutes.PostCreate.route) },
                modifier = Modifier,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus_outline),
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.value.posts) { post ->
                    PostListItem(
                        post = post,
                        onPostClick = { postId -> navController.navigate(PostRoutes.PostDetail.route + "/$postId") },
                        onPostLikeClick = { viewModel.onPostLikeClick(post) },
                        onPostCommentClick = { postId -> navController.navigate(PostRoutes.PostDetail.route + "/$postId?${Constants.PARAM_FOCUS_REPLY}=true") }
                    )
                }
            }
            if (state.value.error.isNotBlank()) {
                Text(text = state.value.error, modifier = Modifier.align(Alignment.Center))
            }
            if (state.value.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            if (!state.value.isLoading && state.value.posts.isEmpty()) {
                Text(text = "No posts...yet", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
