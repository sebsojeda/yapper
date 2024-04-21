package com.github.sebsojeda.yapper.features.post.domain.repository

import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetCreateLikeDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetPostDto

interface PostRepository {
    suspend fun getPosts(): List<GetPostDto>

    suspend fun getPosts(postId: String): List<GetPostDto>

    suspend fun getPost(postId: String): GetPostDto

    suspend fun createPost(post: CreatePostDto): GetPostDto

    suspend fun deletePost(postId: String)

    suspend fun likePost(like: GetCreateLikeDto): GetCreateLikeDto

    suspend fun unlikePost(postId: String, userId: String)
}