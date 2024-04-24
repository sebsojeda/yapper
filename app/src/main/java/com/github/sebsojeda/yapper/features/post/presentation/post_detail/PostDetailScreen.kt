package com.github.sebsojeda.yapper.features.post.presentation.post_detail

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.components.YapperLayout
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.components.CommentListItem
import com.github.sebsojeda.yapper.features.post.presentation.components.MediaPicker
import com.github.sebsojeda.yapper.features.post.presentation.components.MediaRow
import com.github.sebsojeda.yapper.features.post.presentation.components.PostDetail
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun PostDetailScreen(
    navController: NavController,
    viewModel: PostDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val localFocusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    var media by remember { mutableStateOf(emptyList<Uri>()) }

    LaunchedEffect(state.postId) {
        viewModel.getPost(state.postId)
        viewModel.getComments(state.postId)
    }

    YapperLayout(
        navController = navController,
        title = { Text(text = "Post") },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left_outline),
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
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
                            onPostLikeClick = { viewModel.onPostLikeClick(post) },
                            onPostCommentClick = { focusRequester.requestFocus() },
                            onPostReferenceClick = { postId -> navController.navigate(PostRoutes.PostDetail.route + "/$postId") }
                        )
                    }
                }
                if (state.isCommentsLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                items(state.comments) { comment ->
                    CommentListItem(
                        comment = comment,
                        onPostClick = { postId -> navController.navigate(PostRoutes.PostDetail.route + "/$postId") },
                        onPostLikeClick = { viewModel.onCommentLikeClick(comment) },
                        onPostCommentClick = { postId -> navController.navigate(PostRoutes.PostDetail.route + "/$postId?${Constants.PARAM_FOCUS_REPLY}=true") },
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .topBorder(1.dp, Colors.Neutral200)
                    .padding(8.dp),
            ) {
                if (hasFocus) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(text = "Replying to ", color = Colors.Neutral400)
                        Text(text = "@${state.post?.user?.username}", color = Colors.Indigo500)
                    }
                }
                Column(
                    modifier = Modifier.background(color = Colors.Neutral200, shape = MaterialTheme.shapes.extraLarge),
                ) {
                    TextField(
                        value = viewModel.content,
                        onValueChange = { viewModel.onContentChange(it) },
                        placeholder = { Text("Post your reply", color = Colors.Neutral400) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { hasFocus = it.isFocused },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Colors.Transparent,
                            unfocusedIndicatorColor = Colors.Transparent,
                            focusedContainerColor = Colors.Transparent,
                            focusedIndicatorColor = Colors.Transparent,
                        ),
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    MediaRow(media = media, onRemoveMedia = { media = media - it }, height = 150.dp)
                }
                if (hasFocus) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .imePadding(),
                    ) {
                        MediaPicker(
                            modifier = Modifier,
                            onSelectMedia = { media = it }
                        )
                        Button(
                            onClick = {
                                viewModel.onPostCommentClick()
                                localFocusManager.clearFocus()
                                keyboardController?.hide()
                            },
                            enabled = viewModel.content.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Colors.Indigo500,
                                disabledContainerColor = Colors.Indigo300,
                                disabledContentColor = Colors.Indigo500,
                            )
                        ) {
                            Text("Reply")
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(state.focusReply) {
        if (state.focusReply) {
            focusRequester.requestFocus()
        }
    }
}
