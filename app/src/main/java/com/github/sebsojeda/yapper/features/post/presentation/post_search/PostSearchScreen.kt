package com.github.sebsojeda.yapper.features.post.presentation.post_search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.components.YapperLayout
import com.github.sebsojeda.yapper.core.extensions.bottomBorder
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.components.PostListItem
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun PostSearchScreen(
    navController: NavController,
    viewModel: PostSearchViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()
    var search by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    YapperLayout (
        navController = navController,
        title = {
            TextField(
                value = search,
                onValueChange = {
                    search = it
                    viewModel.searchPosts(it)
                },
                placeholder = { Text(text = "Search", color = Colors.Neutral400) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Colors.Neutral200,
                    unfocusedIndicatorColor = Colors.Transparent,
                    focusedContainerColor = Colors.Neutral200,
                    focusedIndicatorColor = Colors.Transparent,
                ),
                shape = MaterialTheme.shapes.extraLarge,
                singleLine = true,
                modifier = Modifier
                    .padding(0.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.magnifying_glass_outline),
                        contentDescription = null,
                        tint = Colors.Neutral400
                    )
                },
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchPosts(search)
                        keyboardController?.hide()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )
        },
        modifier = Modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    if (search.isEmpty()) {
                        Text(text = "Popular", modifier = Modifier.padding(16.dp).bottomBorder(2.dp, Colors.Indigo500))
                    } else {
                        Text(text = "Search results for \"$search\"", color = Colors.Neutral400, modifier = Modifier.padding(16.dp))
                    }
                }
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
