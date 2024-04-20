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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.components.BottomNav
import com.github.sebsojeda.yapper.features.post.presentation.PostDestination
import com.github.sebsojeda.yapper.features.post.presentation.components.PostListItem

@Composable
fun PostsListScreen(
    navController: NavController,
    viewModel: PostListViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = { /*TopNav(user = user)*/ },
        bottomBar = { BottomNav(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            LazyColumn {
                items(state.value.posts) { post ->
                    PostListItem(
                        post = post,
                        onPostClick = { postId -> navController.navigate(PostDestination.PostDetail.route + "/$postId") },
                        onPostLikeClick = { postId -> viewModel.onPostLikeClick(postId) },
                        onPostCommentClick = { postId -> navController.navigate(PostDestination.PostDetail.route + "/$postId") }
                    )
                }
            }
            FloatingActionButton(
                onClick = { navController.navigate(PostDestination.PostCreate.route) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus_outline),
                    contentDescription = null
                )
            }
            if (state.value.error.isNotBlank()) {
                Text(text = state.value.error, modifier = Modifier.align(Alignment.Center))
            }
            if (state.value.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
