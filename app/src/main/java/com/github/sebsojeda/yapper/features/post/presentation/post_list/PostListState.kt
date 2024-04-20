package com.github.sebsojeda.yapper.features.post.presentation.post_list

import com.github.sebsojeda.yapper.features.post.domain.model.Post

data class PostListState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String = ""
)
