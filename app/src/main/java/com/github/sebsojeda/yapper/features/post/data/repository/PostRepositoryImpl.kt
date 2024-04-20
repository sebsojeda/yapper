package com.github.sebsojeda.yapper.features.post.data.repository

import com.github.sebsojeda.yapper.features.post.data.datasource.PostRemoteDataSource
import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetPostDto
import com.github.sebsojeda.yapper.features.post.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postRemoteDataSource: PostRemoteDataSource,
): PostRepository {
    override suspend fun getPosts(): List<GetPostDto> = postRemoteDataSource.getPosts()

    override suspend fun getPosts(postId: String): List<GetPostDto> = postRemoteDataSource.getPosts(postId)

    override suspend fun getPost(postId: String): GetPostDto = postRemoteDataSource.getPost(postId)

    override suspend fun createPost(post: CreatePostDto): GetPostDto = postRemoteDataSource.createPost(post)

    override suspend fun deletePost(postId: String): Unit = postRemoteDataSource.deletePost(postId)
}