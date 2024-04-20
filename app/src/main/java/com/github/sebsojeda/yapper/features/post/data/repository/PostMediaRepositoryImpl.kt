package com.github.sebsojeda.yapper.features.post.data.repository

import com.github.sebsojeda.yapper.features.post.data.datasource.PostMediaRemoteDataSource
import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostMediaDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetPostMediaDto
import com.github.sebsojeda.yapper.features.post.domain.repository.PostMediaRepository
import javax.inject.Inject

class PostMediaRepositoryImpl @Inject constructor(
    private val postMediaRemoteDataSource: PostMediaRemoteDataSource,
): PostMediaRepository {
    override suspend fun createPostMedia(postMedia: List<CreatePostMediaDto>): List<GetPostMediaDto> =
        postMediaRemoteDataSource.createPostMedia(postMedia)
}
