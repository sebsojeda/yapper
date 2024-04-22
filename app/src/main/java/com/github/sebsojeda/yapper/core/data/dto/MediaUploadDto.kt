package com.github.sebsojeda.yapper.core.data.dto

data class MediaUploadDto(
    val data: ByteArray,
    val media: CreateMediaDto
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaUploadDto

        if (!data.contentEquals(other.data)) return false
        if (media != other.media) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + media.hashCode()
        return result
    }
}