package com.github.sebsojeda.yapper.features.post.domain.usecase

import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.post.data.dto.CreateLikeDto
import com.github.sebsojeda.yapper.features.post.domain.repository.PostManager
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ToggleLike @Inject constructor(
    private val postManager: PostManager,
) {
    operator fun invoke(postId: String, isLikedByUser: Boolean): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            if (isLikedByUser) {
                postManager.unlikePost(postId)
            } else {
                postManager.likePost(CreateLikeDto(postId))
            }
            emit(Resource.Success(Unit))
        } catch (e: RestException) {
            emit(Resource.Error(e.error))
        } catch (e: HttpRequestTimeoutException) {
            emit(Resource.Error("The request timed out. Try again later"))
        } catch (e: HttpRequestException) {
            emit(Resource.Error("Unable to connect to the server. Try again later"))
        }
    }
}