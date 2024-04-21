package com.github.sebsojeda.yapper.features.post.domain.usecase

import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.post.data.dto.GetCreateLikeDto
import com.github.sebsojeda.yapper.features.post.domain.repository.PostManager
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LikePost @Inject constructor(
    private val postManager: PostManager,
    private val auth: Auth,
) {
    operator fun invoke(postId: String): Flow<Resource<GetCreateLikeDto>> = flow {
        try {
            emit(Resource.Loading())
            val like = postManager.likePost(GetCreateLikeDto(postId, auth.currentUserOrNull()!!.id))
            emit(Resource.Success(like))
        } catch (e: RestException) {
            emit(Resource.Error(e.error))
        } catch (e: HttpRequestTimeoutException) {
            emit(Resource.Error("The request timed out. Try again later"))
        } catch (e: HttpRequestException) {
            emit(Resource.Error("Unable to connect to the server. Try again later"))
        }
    }
}