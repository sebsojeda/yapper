package com.github.sebsojeda.yapper.features.post.presentation.post_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.components.AppLayout
import com.github.sebsojeda.yapper.core.components.Loading
import com.github.sebsojeda.yapper.core.domain.model.Media
import com.github.sebsojeda.yapper.core.domain.model.MediaUpload
import com.github.sebsojeda.yapper.features.post.domain.model.Post
import com.github.sebsojeda.yapper.features.post.domain.model.PostMedia
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.components.PostCreateDialog
import com.github.sebsojeda.yapper.features.post.presentation.components.PostListItem
import com.github.sebsojeda.yapper.features.user.domain.model.User
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun PostListScreen(
    state: PostListState,
    currentRoute: String?,
    navigateTo: (String) -> Unit,
    toggleLike: (Post) -> Unit,
    createPost: (String, List<MediaUpload>) -> Unit
) {
    val openCreatePostDialog = remember { mutableStateOf(false) }

    if (openCreatePostDialog.value) {
         PostCreateDialog(onCancel = { openCreatePostDialog.value = false }) { content, media ->
            createPost(content, media)
            openCreatePostDialog.value = false
         }
    }

    AppLayout (
        title = { Text(text = "Yapper") },
        currentRoute = currentRoute,
        navigateTo = navigateTo,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openCreatePostDialog.value = true },
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
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            if (state.isLoading) {
                Loading(modifier = Modifier.align(Alignment.Center))
            } else if (state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    modifier = Modifier.align(Alignment.Center),
                    color = Colors.Red500
                )
            } else if (state.posts.isEmpty()) {
                Text(
                    text = "No posts...yet",
                    modifier = Modifier.align(Alignment.Center),
                    color = Colors.Neutral400
                )
            } else {
                LazyColumn {
                    items(state.posts) { post ->
                        PostListItem(
                            post = post,
                            onClick = { navigateTo("${PostRoutes.PostDetail.route}/${post.id}") },
                            onLikeClick = { toggleLike(post) },
                            onCommentClick = { navigateTo("${PostRoutes.PostDetail.route}/${post.id}?${Constants.PARAM_FOCUS_REPLY}=true") },
                            onReferenceClick = { if(post.postReference != null) navigateTo("${PostRoutes.PostDetail.route}/${post.postReference.id}") }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostListScreenPreview() {
    PostListScreen(
        state = PostListState(
            posts = listOf(
                Post(
                    id = "1",
                    userId = "1",
                    user = User(
                        id = "1",
                        name = "Sebastian Ojeda",
                        username = "sebsojeda",
                        avatar = null,
                        createdAt = "2022-01-01T00:00:00Z"
                    ),
                    postId = "1",
                    content = "Hello, world!",
                    likes = 0,
                    comments = 0,
                    createdAt = "2022-01-01T00:00:00Z",
                    likedByUser = false,
                    postMedia = emptyList(),
                    postReference = null
                ),
                Post(
                    id = "2",
                    userId = "2",
                    user = User(
                        id = "2",
                        name = "Jane Doe",
                        username = "janedoe",
                        avatar = null,
                        createdAt = "2022-01-01T00:00:00Z"
                    ),
                    postId = "2",
                    content = "This is a test post.",
                    likes = 0,
                    comments = 0,
                    createdAt = "2022-01-01T00:00:00Z",
                    likedByUser = false,
                    postMedia = listOf(
                        PostMedia(
                            postId = "2",
                            mediaId = "1",
                            media = Media(
                                id = "1",
                                path = "https://picsum.photos/200/300",
                            )
                        )
                    ),
                    postReference = null
                )
            ),
            isLoading = false,
            error = ""
        ),
        currentRoute = PostRoutes.PostList.route,
        navigateTo = {},
        toggleLike = {},
        createPost = {_, _ ->}
    )
}

@Preview(showBackground = true)
@Composable
fun PostListScreenPreviewLoading() {
    PostListScreen(
        state = PostListState(
            posts = emptyList(),
            isLoading = true,
            error = ""
        ),
        currentRoute = PostRoutes.PostList.route,
        navigateTo = {},
        toggleLike = {},
        createPost = {_, _ ->}
    )
}

@Preview(showBackground = true)
@Composable
fun PostListScreenPreviewError() {
    PostListScreen(
        state = PostListState(
            posts = emptyList(),
            isLoading = false,
            error = "Unknown error occurred."
        ),
        currentRoute = PostRoutes.PostList.route,
        navigateTo = {},
        toggleLike = {},
        createPost = {_, _ ->}
    )
}

@Preview(showBackground = true)
@Composable
fun PostListScreenPreviewEmpty() {
    PostListScreen(
        state = PostListState(
            posts = emptyList(),
            isLoading = false,
            error = ""
        ),
        currentRoute = PostRoutes.PostList.route,
        navigateTo = {},
        toggleLike = {},
        createPost = {_, _ ->}
    )
}
