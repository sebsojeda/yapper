package com.github.sebsojeda.yapper.features.post.domain.repository

import com.github.sebsojeda.yapper.features.post.data.dto.CreateLikeDto
import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetLikeDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetPostDto

interface PostRepository {
    suspend fun getPosts(orderColumn: String, orderDescending: Boolean, limit: Long?): List<GetPostDto>

    suspend fun getPosts(postId: String): List<GetPostDto>

    suspend fun getPost(postId: String): GetPostDto

    suspend fun createPost(post: CreatePostDto): GetPostDto

    suspend fun deletePost(postId: String)

    suspend fun likePost(like: CreateLikeDto): GetLikeDto

    suspend fun unlikePost(postId: String, userId: String)

    suspend fun searchPosts(query: String): List<GetPostDto>
}