package com.github.sebsojeda.yapper.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMediaDto(
    @SerialName("file_path") val filePath: String,
    @SerialName("file_size") val fileSize: Int,
)
