package com.github.sebsojeda.yapper.features.post.domain.repository

import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostMediaDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetPostMediaDto

interface PostMediaRepository {
    suspend fun createPostMedia(postMedia: List<CreatePostMediaDto>): List<GetPostMediaDto>
}