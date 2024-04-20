package com.github.sebsojeda.yapper.features.post.domain.repository

import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetPostDto

interface PostManager {
    suspend fun getPosts(): List<GetPostDto>

    suspend fun getPosts(postId: String): List<GetPostDto>

    suspend fun getPost(postId: String): GetPostDto

    suspend fun createPost(post: CreatePostDto, media: List<ByteArray>): GetPostDto

    suspend fun deletePost(postId: String)
}