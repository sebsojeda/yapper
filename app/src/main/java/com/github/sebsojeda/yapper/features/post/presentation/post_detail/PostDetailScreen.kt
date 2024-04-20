package com.github.sebsojeda.yapper.features.post.presentation.post_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.features.post.presentation.PostDestination
import com.github.sebsojeda.yapper.features.post.presentation.components.PostDetail
import com.github.sebsojeda.yapper.features.post.presentation.components.PostListItem

@Composable
fun PostDetailScreen(
    navController: NavController,
    viewModel: PostDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val localFocusManager = LocalFocusManager.current
    var content by remember { mutableStateOf("") }
    var hasFocus by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
    ) {
        if (state.isPostLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .pointerInput(Unit) {
                detectTapGestures { localFocusManager.clearFocus() }
            }
        ) {
            item {
                state.post?.let { post ->
                    PostDetail(
                        post = post,
                        onPostLikeClick = { postId -> viewModel.onPostLikeClick(postId) },
                        onPostCommentClick = {},
                    )
                }
            }
            if (state.isCommentsLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
            items(state.comments) { comment ->
                PostListItem(
                    post = comment,
                    onPostClick = { postId -> navController.navigate(PostDestination.PostDetail.route + "/$postId") },
                    onPostLikeClick = { postId -> viewModel.onPostLikeClick(postId) },
                    onPostCommentClick = { postId -> navController.navigate(PostDestination.PostDetail.route + "/$postId") }
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .topBorder(1.dp, Color.LightGray)
                .padding(8.dp),
        ) {
            if (hasFocus) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 8.dp),
                    enabled = content.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Reply")
                }
            }
            TextField(
                value = content,
                onValueChange = { content = it },
                placeholder = { Text("Post your reply") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { hasFocus = it.isFocused },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                ),
                shape = MaterialTheme.shapes.extraLarge,
            )
        }
    }
}
