package com.github.sebsojeda.yapper.core.data.repository

import com.github.sebsojeda.yapper.core.data.datasource.MediaRemoteDataSource
import com.github.sebsojeda.yapper.core.data.dto.CreateMediaDto
import com.github.sebsojeda.yapper.core.data.dto.GetMediaDto
import com.github.sebsojeda.yapper.core.domain.repository.MediaRepository
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val mediaRemoteDataSource: MediaRemoteDataSource,
): MediaRepository {
    override suspend fun createMedia(media: List<CreateMediaDto>): List<GetMediaDto> =
        mediaRemoteDataSource.createMedia(media)
}
