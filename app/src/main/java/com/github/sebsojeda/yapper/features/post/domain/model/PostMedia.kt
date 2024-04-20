package com.github.sebsojeda.yapper.features.post.domain.model

import com.github.sebsojeda.yapper.core.domain.model.Media

data class PostMedia(
    val postId: String,
    val mediaId: String,
    val media: Media,
)
