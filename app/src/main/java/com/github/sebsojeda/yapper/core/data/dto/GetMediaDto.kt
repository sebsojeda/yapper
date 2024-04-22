package com.github.sebsojeda.yapper.core.data.dto

import com.github.sebsojeda.yapper.core.domain.model.Media
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMediaDto(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("file_path") val filePath: String,
    @SerialName("file_size") val fileSize: Int,
    @SerialName("created_at") val createdAt: String,
)

fun GetMediaDto.toMedia() = Media(
    id = id,
    path = filePath,
)
