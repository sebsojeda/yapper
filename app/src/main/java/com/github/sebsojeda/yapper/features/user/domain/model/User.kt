package com.github.sebsojeda.yapper.features.user.domain.model

import com.github.sebsojeda.yapper.core.domain.model.Media

data class User(
    val id: String,
    val username: String,
    val name: String,
    val createdAt: String,
    val avatar: Media?,
)
