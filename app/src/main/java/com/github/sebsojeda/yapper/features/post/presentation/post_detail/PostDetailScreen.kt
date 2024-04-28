package com.github.sebsojeda.yapper.features.post.presentation.post_detail

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.components.AppLayout
import com.github.sebsojeda.yapper.core.components.RoundedInputField
import com.github.sebsojeda.yapper.core.domain.model.MediaUpload
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.components.CommentListItem
import com.github.sebsojeda.yapper.features.post.presentation.components.MediaPicker
import com.github.sebsojeda.yapper.features.post.presentation.components.MediaRow
import com.github.sebsojeda.yapper.features.post.presentation.components.PostDetail
import com.github.sebsojeda.yapper.ui.theme.Colors
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
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
    var content by remember { mutableStateOf("") }
    val contentResolver = LocalContext.current.contentResolver
    val interactionSource = remember { MutableInteractionSource() }

    AppLayout(
        navController = navController,
        title = { Text(text = "Post") },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigateUp()
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
            modifier = Modifier.padding(innerPadding)
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
                            onPostLikeClick = { viewModel.onToggleLike(post) },
                            onPostCommentClick = { focusRequester.requestFocus() },
                            onPostReferenceClick = { postId -> navController.navigate(PostRoutes.PostDetail.route + "/$postId") }
                        )
                    }
                }
                if (state.areCommentsLoading) {
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
                        onPostLikeClick = { viewModel.onToggleCommentLike(comment) },
                        onPostCommentClick = { postId -> navController.navigate(PostRoutes.PostDetail.route + "/$postId?${Constants.PARAM_FOCUS_REPLY}=true") },
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .consumeWindowInsets(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                    .imePadding()
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
                    RoundedInputField(
                        value = content,
                        onValueChange = { content = it },
                        placeholder = "Post your reply",
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { hasFocus = it.isFocused }
                    )
                    MediaRow(media = media, onRemoveMedia = { media = media - it }, height = 150.dp)
                }
                if (hasFocus) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        MediaPicker(
                            modifier = Modifier,
                            onSelectMedia = { media = it }
                        )
                        TextButton(
                            onClick = {
                                val uploads = mutableListOf<MediaUpload>()
                                media.forEach { uri ->
                                    contentResolver.openInputStream(uri)?.buffered()?.let {
                                        val filePath = UUID.randomUUID().toString()
                                        val data = it.readBytes()
                                        val upload = MediaUpload(
                                            data = data,
                                            filePath = filePath,
                                            fileSize = data.size
                                        )
                                        uploads.add(upload)
                                    }
                                }
                                viewModel.onPostCommentClick(content, uploads)
                                content = ""
                                media = emptyList()
                                localFocusManager.clearFocus()
                                keyboardController?.hide()
                            },
                            enabled = content.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Colors.Indigo500,
                                disabledContainerColor = Colors.Indigo300,
                                disabledContentColor = Colors.Indigo500,
                                contentColor = Colors.White
                            ),
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
