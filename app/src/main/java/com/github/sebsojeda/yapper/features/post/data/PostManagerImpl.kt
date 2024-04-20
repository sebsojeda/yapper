package com.github.sebsojeda.yapper.features.post.data

import com.github.sebsojeda.yapper.core.data.dto.CreateMediaDto
import com.github.sebsojeda.yapper.core.domain.repository.MediaRepository
import com.github.sebsojeda.yapper.core.domain.repository.MediaStorageRepository
import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostDto
import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostMediaDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetPostDto
import com.github.sebsojeda.yapper.features.post.domain.repository.PostManager
import com.github.sebsojeda.yapper.features.post.domain.repository.PostMediaRepository
import com.github.sebsojeda.yapper.features.post.domain.repository.PostRepository
import java.util.UUID
import javax.inject.Inject

class PostManagerImpl @Inject constructor(
    private val postRepository: PostRepository,
    private val mediaRepository: MediaRepository,
    private val postMediaRepository: PostMediaRepository,
    private val mediaStorageRepository: MediaStorageRepository,
): PostManager {
    override suspend fun getPosts(): List<GetPostDto> = postRepository.getPosts()

    override suspend fun getPosts(postId: String): List<GetPostDto> = postRepository.getPosts(postId)

    override suspend fun getPost(postId: String): GetPostDto = postRepository.getPost(postId)

    override suspend fun createPost(post: CreatePostDto, media: List<ByteArray>): GetPostDto {
        val newPost = postRepository.createPost(post)

        // Create media
        val createMedia = mutableListOf<CreateMediaDto>()
        media.forEach { bytes ->
            val filePath = UUID.randomUUID().toString()
            mediaStorageRepository.uploadMedia(filePath, bytes)
            createMedia.add(
                CreateMediaDto(
                    filePath = filePath,
                    fileSize = bytes.size,
                )
            )
        }
        val newMedia = mediaRepository.createMedia(createMedia)

        // Create post media
        val createPostMedia = mutableListOf<CreatePostMediaDto>()
        newMedia.forEach {
            createPostMedia.add(
                CreatePostMediaDto(
                    mediaId = it.id,
                    postId = newPost.id,
                )
            )
        }
        val newPostMedia = postMediaRepository.createPostMedia(createPostMedia)

        return newPost.copy(postMedia = newPostMedia)
    }

    override suspend fun deletePost(postId: String) {
        val post = postRepository.getPost(postId)
        postRepository.deletePost(postId)
        post.postMedia.forEach {
            mediaStorageRepository.deleteMedia(it.media.filePath)
        }
    }
}
