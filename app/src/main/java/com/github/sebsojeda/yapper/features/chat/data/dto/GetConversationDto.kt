package com.github.sebsojeda.yapper.features.chat.data.dto

import com.github.sebsojeda.yapper.core.data.dto.GetMediaDto
import com.github.sebsojeda.yapper.core.data.dto.toMedia
import com.github.sebsojeda.yapper.features.chat.domain.model.Conversation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetConversationDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String?,
    @SerialName("media_id") val mediaId: String?,
    @SerialName("media") val media: GetMediaDto?,
    @SerialName("created_at") val createdAt: String,
)

fun GetConversationDto.toConversation() = Conversation(
    id = id,
    name = name,
    mediaId = mediaId,
    media = media?.toMedia(),
    createdAt = createdAt,
)