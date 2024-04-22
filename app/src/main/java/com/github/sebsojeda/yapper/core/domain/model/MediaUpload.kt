package com.github.sebsojeda.yapper.core.domain.model

import com.github.sebsojeda.yapper.core.data.dto.CreateMediaDto
import com.github.sebsojeda.yapper.core.data.dto.MediaUploadDto

data class MediaUpload(
    val data: ByteArray,
    val filePath: String,
    val fileSize: Int,

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaUpload

        if (!data.contentEquals(other.data)) return false
        if (filePath != other.filePath) return false
        if (fileSize != other.fileSize) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + filePath.hashCode()
        result = 31 * result + fileSize
        return result
    }

}

fun MediaUpload.toMediaUploadDto() = MediaUploadDto(
    data = data,
    media = CreateMediaDto(
        filePath = filePath,
        fileSize = fileSize
    )
)
