package com.github.sebsojeda.yapper.features.post.presentation.post_search

import com.github.sebsojeda.yapper.features.post.domain.model.Post

data class PostSearchState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String = ""
)
