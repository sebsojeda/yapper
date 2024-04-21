package com.github.sebsojeda.yapper.features.post.domain.usecase

import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostDto
import com.github.sebsojeda.yapper.features.post.data.dto.toPost
import com.github.sebsojeda.yapper.features.post.domain.model.Post
import com.github.sebsojeda.yapper.features.post.domain.repository.PostManager
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreatePost @Inject constructor(
    private val postManager: PostManager,
    private val auth: Auth
){
    operator fun invoke(content: String, postId: String?, media: List<ByteArray> = emptyList()): Flow<Resource<Post>> = flow {
        emit(Resource.Loading())
        try {
            val post = CreatePostDto(userId = auth.currentUserOrNull()!!.id, postId = postId, content = content)
            val createdPost = postManager.createPost(post, media).toPost()
            emit(Resource.Success(createdPost))
        } catch (e: RestException) {
            emit(Resource.Error(e.error))
        } catch (e: HttpRequestTimeoutException) {
            emit(Resource.Error("The request timed out. Try again later"))
        } catch (e: HttpRequestException) {
            emit(Resource.Error("Unable to connect to the server. Try again later"))
        }
    }
}