package com.github.sebsojeda.yapper.features.post.presentation.post_detail

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.components.AppLayout
import com.github.sebsojeda.yapper.core.components.Loading
import com.github.sebsojeda.yapper.core.components.RoundedInputField
import com.github.sebsojeda.yapper.core.domain.model.Media
import com.github.sebsojeda.yapper.core.domain.model.MediaUpload
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.features.post.domain.model.Comment
import com.github.sebsojeda.yapper.features.post.domain.model.Post
import com.github.sebsojeda.yapper.features.post.domain.model.PostMedia
import com.github.sebsojeda.yapper.features.post.domain.model.PostReference
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.components.CommentListItem
import com.github.sebsojeda.yapper.features.post.presentation.components.MediaPicker
import com.github.sebsojeda.yapper.features.post.presentation.components.MediaRow
import com.github.sebsojeda.yapper.features.post.presentation.components.PostDetail
import com.github.sebsojeda.yapper.features.user.domain.model.User
import com.github.sebsojeda.yapper.ui.theme.Colors
import java.util.UUID

@Composable
fun PostDetailScreen(
    state: PostDetailState,
    currentRoute: String?,
    navigateTo: (String) -> Unit,
    navigateBack: () -> Unit,
    toggleLike: (Post) -> Unit,
    toggleCommentLike: (Comment) -> Unit,
    createComment: (String, List<MediaUpload>) -> Unit,
) {
    val localFocusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var focusReply by remember { mutableStateOf(state.focusReply) }
    var selectedMedia by remember { mutableStateOf(emptyList<Uri>()) }
    var content by remember { mutableStateOf("") }
    val contentResolver = LocalContext.current.contentResolver

    AppLayout(
        title = { Text(text = "Post") },
        currentRoute = currentRoute,
        navigateTo = navigateTo,
        navigationIcon = {
            IconButton(
                onClick = navigateBack,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left_outline),
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            if (state.isPostLoading) {
                Loading(modifier = Modifier.align(Alignment.Center))
            } else if (state.postError.isNotEmpty()) {
                Text(
                    text = state.postError,
                    modifier = Modifier.align(Alignment.Center),
                    color = Colors.Red500
                )
            } else
            Column {
                LazyColumn(modifier = Modifier
                    .weight(1f)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            localFocusManager.clearFocus()
                        }
                    }
                ) {
                    item {
                        state.post?.let { post ->
                            PostDetail(
                                post = post,
                                onPostLikeClick = { toggleLike(post) },
                                onPostCommentClick = { focusReply = true },
                                onPostReferenceClick = { if (post.postReference != null) navigateTo("${PostRoutes.PostDetail.route}/${post.postReference.id}") }
                            )
                        }
                    }
                    if (state.areCommentsLoading) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth()
                            ) {
                                Loading(modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    } else if (state.commentsError.isNotEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = state.commentsError,
                                    modifier = Modifier.align(Alignment.Center),
                                    color = Colors.Red500
                                )
                            }
                        }

                    } else {
                        items(state.comments) { comment ->
                            CommentListItem(
                                comment = comment,
                                onPostClick = { postId -> navigateTo(PostRoutes.PostDetail.route + "/$postId") },
                                onPostLikeClick = { toggleCommentLike(comment) },
                                onPostCommentClick = { postId -> navigateTo(PostRoutes.PostDetail.route + "/$postId?${Constants.PARAM_FOCUS_REPLY}=true") },
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .consumeWindowInsets(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                        .imePadding()
                        .topBorder(1.dp, Colors.Neutral200)
                        .padding(8.dp),
                ) {
                    if (focusReply) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(text = "Replying to ", color = Colors.Neutral400)
                            Text(text = "@${state.post?.user?.username}", color = Colors.Indigo500)
                        }
                    }
                    Column(
                        modifier = Modifier.background(
                            color = Colors.Neutral200,
                            shape = RoundedCornerShape(32.dp)
                        )
                    ) {
                        RoundedInputField(
                            value = content,
                            onValueChange = { content = it },
                            placeholder = "Post your reply",
                            focus = focusReply,
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { focusReply = it.isFocused }
                        )
                        MediaRow(
                            media = selectedMedia,
                            onRemoveMedia = { selectedMedia = selectedMedia - it },
                            height = 150.dp
                        )
                    }
                    if (focusReply) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            MediaPicker(onSelectMedia = { selectedMedia = it })
                            TextButton(
                                onClick = {
                                    val uploads = mutableListOf<MediaUpload>()
                                    selectedMedia.forEach { uri ->
                                        contentResolver.openInputStream(uri)?.buffered()?.let {
                                            val filePath = UUID.randomUUID().toString()
                                            val data = it.readBytes()
                                            val media = MediaUpload(
                                                data = data,
                                                filePath = filePath,
                                                fileSize = data.size
                                            )
                                            uploads.add(media)
                                        }
                                    }
                                    createComment(content, uploads)
                                    content = ""
                                    selectedMedia = emptyList()
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
    }
}

@Preview(showBackground = true)
@Composable
fun PostDetailScreenPreview() {
    PostDetailScreen(
        state = PostDetailState(
            postId = "1",
            focusReply = true,
            isPostLoading = false,
            areCommentsLoading = false,
            post = Post(
                id = "1",
                userId = "1",
                user = User(
                    id = "1",
                    name = "User 1",
                    username = "user1",
                    avatar = null,
                    createdAt = "2022-01-01T00:00:00Z"
                ),
                postId = "1",
                postReference = PostReference(
                    id = "1",
                    userId = "1",
                    user = User(
                        id = "1",
                        name = "User 3",
                        username = "user3",
                        createdAt = "2022-01-01T00:00:00Z",
                        avatar = null
                    ),
                    content = "This is a post",
                    createdAt = "2021-01-01T00:00:00Z",
                    postMedia = emptyList()
                ),
                content = "This is a post",
                likes = 1,
                comments = 1,
                createdAt = "2021-01-01T00:00:00Z",
                likedByUser = false,
                postMedia = listOf(
                    PostMedia(
                        postId = "1",
                        mediaId = "1",
                        media = Media(
                            id = "1",
                            path = "https://example.com/image1.jpg"
                        )
                    )
                )
            ),
            comments = listOf(
                Comment(
                    id = "1",
                    userId = "2",
                    user = User(
                        id = "2",
                        name = "User 2",
                        username = "user2",
                        createdAt = "2022-01-01T00:00:00Z",
                        avatar = null
                    ),
                    postId = "1",
                    content = "This is a comment",
                    likes = 1,
                    comments = 0,
                    createdAt = "2021-01-01T00:00:00Z",
                    likedByUser = true,
                    postMedia = emptyList()
                )
            ),
            postError = "",
            commentsError = ""
        ),
        currentRoute = PostRoutes.PostDetail.route,
        navigateTo = {},
        navigateBack = {},
        toggleLike = {},
        toggleCommentLike = {},
        createComment = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPostDetailScreenPostLoading() {
    PostDetailScreen(
        state = PostDetailState(
            postId = "1",
            focusReply = false,
            isPostLoading = true,
            areCommentsLoading = false,
            post = null,
            comments = emptyList(),
            postError = "",
            commentsError = ""
        ),
        currentRoute = PostRoutes.PostDetail.route,
        navigateTo = {},
        navigateBack = {},
        toggleLike = {},
        toggleCommentLike = {},
        createComment = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPostDetailScreenPostError() {
    PostDetailScreen(
        state = PostDetailState(
            postId = "1",
            focusReply = false,
            isPostLoading = false,
            areCommentsLoading = false,
            post = null,
            comments = emptyList(),
            postError = "An error occurred",
            commentsError = ""
        ),
        currentRoute = PostRoutes.PostDetail.route,
        navigateTo = {},
        navigateBack = {},
        toggleLike = {},
        toggleCommentLike = {},
        createComment = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPostDetailScreenCommentsLoading() {
    PostDetailScreen(
        state = PostDetailState(
            postId = "1",
            focusReply = false,
            isPostLoading = false,
            areCommentsLoading = true,
            post = Post(
                id = "1",
                userId = "1",
                user = User(
                    id = "1",
                    name = "User 1",
                    username = "user1",
                    avatar = null,
                    createdAt = "2022-01-01T00:00:00Z"
                ),
                postId = "1",
                postReference = null,
                content = "This is a post",
                likes = 1,
                comments = 1,
                createdAt = "2021-01-01T00:00:00Z",
                likedByUser = false,
                postMedia = emptyList()
            ),
            comments = emptyList(),
            postError = "",
            commentsError = ""
        ),
        currentRoute = PostRoutes.PostDetail.route,
        navigateTo = {},
        navigateBack = {},
        toggleLike = {},
        toggleCommentLike = {},
        createComment = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPostDetailScreenCommentsError() {
    PostDetailScreen(
        state = PostDetailState(
            postId = "1",
            focusReply = false,
            isPostLoading = false,
            areCommentsLoading = false,
            post = Post(
                id = "1",
                userId = "1",
                user = User(
                    id = "1",
                    name = "User 1",
                    username = "user1",
                    avatar = null,
                    createdAt = "2022-01-01T00:00:00Z"
                ),
                postId = "1",
                postReference = null,
                content = "This is a post",
                likes = 1,
                comments = 1,
                createdAt = "2021-01-01T00:00:00Z",
                likedByUser = false,
                postMedia = emptyList()
            ),
            comments = emptyList(),
            postError = "",
            commentsError = "An error occurred"
        ),
        currentRoute = PostRoutes.PostDetail.route,
        navigateTo = {},
        navigateBack = {},
        toggleLike = {},
        toggleCommentLike = {},
        createComment = { _, _ -> }
    )
}
