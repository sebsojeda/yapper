package com.github.sebsojeda.yapper.features.post.presentation.post_detail

import com.github.sebsojeda.yapper.features.post.domain.model.Post

data class PostDetailState(
    val isPostLoading: Boolean = false,
    val isCommentsLoading: Boolean = false,

    val post: Post? = null,
    val comments: List<Post> = emptyList(),

    val postError: String = "",
    val commentsError: String = "",
)