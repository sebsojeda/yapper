package com.github.sebsojeda.yapper.features.post.presentation.post_create

data class PostCreateState(
    val isLoading: Boolean = false,
    val isPostCreated: Boolean = false,
    val error: String = "",
    val postId: String? = null,
)
