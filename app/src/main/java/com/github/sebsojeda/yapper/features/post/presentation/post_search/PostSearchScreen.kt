package com.github.sebsojeda.yapper.features.post.presentation.post_search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.components.AppLayout
import com.github.sebsojeda.yapper.core.components.RoundedInputField
import com.github.sebsojeda.yapper.core.extensions.bottomBorder
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.components.CommentListItem
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun PostSearchScreen(
    navController: NavController,
    viewModel: PostSearchViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()
    val search = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    AppLayout (
        navController = navController,
        title = {
            RoundedInputField(
                value = search.value,
                onValueChange = {
                    search.value = it
                    viewModel.searchPosts(it)
                },
                placeholder = "Search",
                modifier = Modifier.width(200.dp),
                singleLine = true,
                prefix = {
                    Icon(
                        painter = painterResource(id = R.drawable.magnifying_glass_outline),
                        contentDescription = null,
                        tint = Colors.Neutral400,
                    )
                },
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchPosts(search.value)
                        keyboardController?.hide()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(PostRoutes.PostCreate.route) },
                shape = CircleShape,
                containerColor = Colors.Indigo500,
                contentColor = Colors.White
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
                item {
                    if (search.value.isEmpty()) {
                        Text(text = "Popular", modifier = Modifier
                            .padding(16.dp)
                            .bottomBorder(2.dp, Colors.Indigo500))
                    } else {
                        Text(text = "Search results for \"${search.value}\"", color = Colors.Neutral400, modifier = Modifier.padding(16.dp))
                    }
                }
                items(state.value.posts) { post ->
                    CommentListItem(
                        post = post,
                        onPostClick = { postId -> navController.navigate(PostRoutes.PostDetail.route + "/$postId") },
                        onPostLikeClick = { viewModel.onToggleLike(post) },
                        onPostCommentClick = { postId -> navController.navigate(PostRoutes.PostDetail.route + "/$postId?${Constants.PARAM_FOCUS_REPLY}=true") },
                        onPostReferenceClick = { postId -> navController.navigate(PostRoutes.PostDetail.route + "/$postId") }
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
                Text(text = "No posts...yet",
                    modifier = Modifier.align(Alignment.Center),
                    color = Colors.Neutral400
                )
            }
        }
    }
}
