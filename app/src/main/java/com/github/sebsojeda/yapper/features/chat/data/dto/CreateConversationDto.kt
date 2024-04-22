package com.github.sebsojeda.yapper.features.chat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateConversationDto(
    @SerialName("name") val name: String?,
    @SerialName("media_id") val mediaId: String?,
)
