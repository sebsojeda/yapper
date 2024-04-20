package com.github.sebsojeda.yapper.core.data.repository

import com.github.sebsojeda.yapper.core.data.datasource.MediaStorageRemoteDataSource
import com.github.sebsojeda.yapper.core.domain.repository.MediaStorageRepository
import javax.inject.Inject

class MediaStorageRepositoryImpl @Inject constructor(
    private val mediaStorageRemoteDataSource: MediaStorageRemoteDataSource,
): MediaStorageRepository {
    override suspend fun uploadMedia(path: String, data: ByteArray): Unit =
        mediaStorageRemoteDataSource.uploadMedia(path, data)

    override suspend fun deleteMedia(path: String): Unit =
        mediaStorageRemoteDataSource.deleteMedia(path)
}
