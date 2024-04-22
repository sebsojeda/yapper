package com.github.sebsojeda.yapper.features.post.domain.usecase

import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.post.data.dto.toPost
import com.github.sebsojeda.yapper.features.post.domain.model.Post
import com.github.sebsojeda.yapper.features.post.domain.repository.PostManager
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPosts @Inject constructor(
    private val postManager: PostManager,
) {
    operator fun invoke(refreshInterval: Long, orderColumn: String = "created_at", orderDescending: Boolean = true, limit: Long? = null): Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())
            while (true) {
                val posts = postManager.getPosts(orderColumn, orderDescending, limit).map { it.toPost() }
                emit(Resource.Success(posts))
                delay(refreshInterval)
            }
        } catch (e: RestException) {
            emit(Resource.Error(e.error))
        } catch (e: HttpRequestTimeoutException) {
            emit(Resource.Error("The request timed out. Try again later"))
        } catch (e: HttpRequestException) {
            emit(Resource.Error("Unable to connect to the server. Try again later"))
        }
    }
}