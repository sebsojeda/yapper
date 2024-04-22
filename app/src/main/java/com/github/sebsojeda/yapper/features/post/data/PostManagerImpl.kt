package com.github.sebsojeda.yapper.features.post.data

import com.github.sebsojeda.yapper.core.data.dto.MediaUploadDto
import com.github.sebsojeda.yapper.core.domain.repository.MediaRepository
import com.github.sebsojeda.yapper.core.domain.repository.MediaStorageRepository
import com.github.sebsojeda.yapper.features.post.data.dto.CreateLikeDto
import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostDto
import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostMediaDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetLikeDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetPostDto
import com.github.sebsojeda.yapper.features.post.domain.repository.PostManager
import com.github.sebsojeda.yapper.features.post.domain.repository.PostMediaRepository
import com.github.sebsojeda.yapper.features.post.domain.repository.PostRepository
import io.github.jan.supabase.gotrue.Auth
import javax.inject.Inject

class PostManagerImpl @Inject constructor(
    private val postRepository: PostRepository,
    private val mediaRepository: MediaRepository,
    private val postMediaRepository: PostMediaRepository,
    private val mediaStorageRepository: MediaStorageRepository,
    private val auth: Auth,
): PostManager {
    override suspend fun getPosts(orderColumn: String, orderDescending: Boolean, limit: Long?): List<GetPostDto> =
        postRepository.getPosts(orderColumn, orderDescending, limit)

    override suspend fun getPosts(postId: String): List<GetPostDto> = postRepository.getPosts(postId)

    override suspend fun getPost(postId: String): GetPostDto = postRepository.getPost(postId)

    override suspend fun createPost(post: CreatePostDto, media: List<MediaUploadDto>): GetPostDto {
        // Create post
        val newPost = postRepository.createPost(post)

        // Upload media
        media.forEach {
            mediaStorageRepository.uploadMedia(it.media.filePath, it.data)
        }

        // Create media
        val newMedia = mediaRepository.createMedia(media.map { it.media })

        // Create post media
        val newPostMedia = postMediaRepository.createPostMedia(newMedia.map {
            CreatePostMediaDto(postId = newPost.id, mediaId = it.id)
        })

        return newPost.copy(postMedia = newPostMedia)
    }

    override suspend fun deletePost(postId: String) {
        val post = postRepository.getPost(postId)
        postRepository.deletePost(postId)
        post.postMedia.forEach {
            mediaStorageRepository.deleteMedia(it.media.filePath)
        }
    }

    override suspend fun likePost(like: CreateLikeDto): GetLikeDto =
        postRepository.likePost(like)

    override suspend fun unlikePost(postId: String) =
        postRepository.unlikePost(postId, auth.currentUserOrNull()!!.id)

    override suspend fun searchPosts(query: String): List<GetPostDto> =
        postRepository.searchPosts(query)
}
