package com.github.sebsojeda.yapper.core.domain.repository

interface MediaStorageRepository {
    suspend fun uploadMedia(path: String, data: ByteArray)

    suspend fun deleteMedia(path: String)
}