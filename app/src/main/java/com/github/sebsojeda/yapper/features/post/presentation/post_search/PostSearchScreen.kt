package com.github.sebsojeda.yapper.features.post.presentation.post_search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.components.AppLayout
import com.github.sebsojeda.yapper.core.components.Loading
import com.github.sebsojeda.yapper.core.components.RoundedInputField
import com.github.sebsojeda.yapper.core.domain.model.MediaUpload
import com.github.sebsojeda.yapper.core.extensions.bottomBorder
import com.github.sebsojeda.yapper.features.post.domain.model.Post
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.components.PostCreateDialog
import com.github.sebsojeda.yapper.features.post.presentation.components.PostListItem
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun PostSearchScreen(
    state: PostSearchState,
    currentRoute: String?,
    navigateTo: (String) -> Unit,
    toggleLike: (Post) -> Unit,
    search: (String) -> Unit,
    createPost: (String, List<MediaUpload>) -> Unit
) {
    val query = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val openCreatePostDialog = remember { mutableStateOf(false) }

    if (openCreatePostDialog.value) {
        PostCreateDialog(onCancel = { openCreatePostDialog.value = false }) { content, media ->
            createPost(content, media)
            openCreatePostDialog.value = false
        }
    }

    AppLayout (
        title = {
            RoundedInputField(
                value = query.value,
                onValueChange = {
                    query.value = it
                    search(it)
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
                        search(query.value)
                        keyboardController?.hide()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )
        },
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
                    color = Colors.Red500,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.posts.isEmpty()) {
                Text(text = "No posts...yet",
                    modifier = Modifier.align(Alignment.Center),
                    color = Colors.Neutral400
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        if (query.value.isEmpty()) {
                            Text(text = "Popular", modifier = Modifier
                                .padding(16.dp)
                                .bottomBorder(2.dp, Colors.Indigo500))
                        } else {
                            Text(text = "Search results for \"${query.value}\"", color = Colors.Neutral400, modifier = Modifier.padding(16.dp))
                        }
                    }
                    items(state.posts) { post ->
                        PostListItem(
                            post = post,
                            onClick = { navigateTo("${PostRoutes.PostDetail.route}/${post.id}") },
                            onLikeClick = { toggleLike(post) },
                            onCommentClick = { navigateTo("${PostRoutes.PostDetail.route}/${post.id}?${Constants.PARAM_FOCUS_REPLY}=true") },
                            onReferenceClick = { if (post.postReference != null) navigateTo("${PostRoutes.PostDetail.route}/${post.postReference.id}") }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostSearchScreenPreviewLoading() {
    PostSearchScreen(
        state = PostSearchState(
            isLoading = true
        ),
        currentRoute = PostRoutes.PostSearch.route,
        navigateTo = {},
        toggleLike = {},
        search = { },
        createPost = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun PostSearchScreenPreviewError() {
    PostSearchScreen(
        state = PostSearchState(
            error = "An unexpected error occurred"
        ),
        currentRoute = PostRoutes.PostSearch.route,
        navigateTo = {},
        toggleLike = {},
        search = { },
        createPost = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun PostSearchScreenPreviewEmpty() {
    PostSearchScreen(
        state = PostSearchState(),
        currentRoute = PostRoutes.PostSearch.route,
        navigateTo = {},
        toggleLike = {},
        search = { },
        createPost = { _, _ -> }
    )
}
