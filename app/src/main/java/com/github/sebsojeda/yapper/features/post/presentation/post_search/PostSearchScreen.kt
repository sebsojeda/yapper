package com.github.sebsojeda.yapper.features.post.presentation.post_search

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.components.YapperLayout
import com.github.sebsojeda.yapper.core.extensions.bottomBorder
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.components.CommentListItem
import com.github.sebsojeda.yapper.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSearchScreen(
    navController: NavController,
    viewModel: PostSearchViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()
    var search by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }

    YapperLayout (
        navController = navController,
        title = {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .background(Colors.Neutral200)
            ) {
                BasicTextField(
                    value = search,
                    onValueChange = {
                        search = it
                        viewModel.searchPosts(it)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .clip(RoundedCornerShape(32.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .width(240.dp),
                    decorationBox = @Composable { innerTextField ->
                        TextFieldDefaults.DecorationBox(
                            value = search,
                            innerTextField = innerTextField,
                            enabled = true,
                            singleLine = false,
                            visualTransformation = VisualTransformation.None,
                            interactionSource = interactionSource,
                            placeholder = { Text(text = "Search", color = Colors.Neutral400) },
                            contentPadding = PaddingValues(),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Colors.Transparent,
                                unfocusedIndicatorColor = Colors.Transparent,
                                focusedContainerColor = Colors.Transparent,
                                focusedIndicatorColor = Colors.Transparent,
                            ),
                            prefix = {
                                Icon(
                                    painter = painterResource(id = R.drawable.magnifying_glass_outline),
                                    contentDescription = null,
                                    tint = Colors.Neutral400,
                                )
                            }
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
            }
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
                        Text(text = "Popular", modifier = Modifier
                            .padding(16.dp)
                            .bottomBorder(2.dp, Colors.Indigo500))
                    } else {
                        Text(text = "Search results for \"$search\"", color = Colors.Neutral400, modifier = Modifier.padding(16.dp))
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
