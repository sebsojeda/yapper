package com.github.sebsojeda.yapper.features.chat.domain.model

import com.github.sebsojeda.yapper.core.domain.model.Media

data class Conversation(
    val id: String,
    val name: String,
    val mediaId: String?,
    val media: Media?,
    val messages: List<Message>?,
    val participants: List<Participant>?,
    val createdAt: String,
)
