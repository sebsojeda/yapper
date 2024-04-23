package com.github.sebsojeda.yapper.features.post.presentation.post_detail

import com.github.sebsojeda.yapper.features.post.domain.model.Comment
import com.github.sebsojeda.yapper.features.post.domain.model.Post

data class PostDetailState(
    val postId: String = "",
    val focusReply: Boolean = false,

    val isPostLoading: Boolean = false,
    val isCommentsLoading: Boolean = false,

    val post: Post? = null,
    val comments: List<Comment> = emptyList(),

    val postError: String = "",
    val commentsError: String = "",
)