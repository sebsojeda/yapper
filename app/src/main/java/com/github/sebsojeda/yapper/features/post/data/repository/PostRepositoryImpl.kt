package com.github.sebsojeda.yapper.features.post.data.repository

import com.github.sebsojeda.yapper.features.post.data.datasource.PostRemoteDataSource
import com.github.sebsojeda.yapper.features.post.data.dto.CreateLikeDto
import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetLikeDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetPostDto
import com.github.sebsojeda.yapper.features.post.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postRemoteDataSource: PostRemoteDataSource,
): PostRepository {
    override suspend fun getPosts(orderColumn: String, orderDescending: Boolean, limit: Long?): List<GetPostDto> =
        postRemoteDataSource.getPosts(orderColumn, orderDescending, limit)

    override suspend fun getPosts(postId: String): List<GetPostDto> =
        postRemoteDataSource.getPosts(postId)

    override suspend fun getPost(postId: String): GetPostDto =
        postRemoteDataSource.getPost(postId)

    override suspend fun createPost(post: CreatePostDto): GetPostDto =
        postRemoteDataSource.createPost(post)

    override suspend fun deletePost(postId: String): Unit =
        postRemoteDataSource.deletePost(postId)

    override suspend fun likePost(like: CreateLikeDto): GetLikeDto =
        postRemoteDataSource.likePost(like)

    override suspend fun unlikePost(postId: String, userId: String): Unit =
        postRemoteDataSource.unlikePost(postId, userId)

    override suspend fun searchPosts(query: String): List<GetPostDto> =
        postRemoteDataSource.searchPosts(query)
}