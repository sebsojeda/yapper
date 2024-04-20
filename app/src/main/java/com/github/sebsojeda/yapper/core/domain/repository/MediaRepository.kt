package com.github.sebsojeda.yapper.core.domain.repository

import com.github.sebsojeda.yapper.core.data.dto.CreateMediaDto
import com.github.sebsojeda.yapper.core.data.dto.GetMediaDto

interface MediaRepository {
    suspend fun createMedia(media: List<CreateMediaDto>): List<GetMediaDto>
}