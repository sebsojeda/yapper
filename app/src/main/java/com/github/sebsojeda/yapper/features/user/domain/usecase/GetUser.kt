package com.github.sebsojeda.yapper.features.user.domain.usecase

import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.user.data.dto.toUser
import com.github.sebsojeda.yapper.features.user.domain.model.User
import com.github.sebsojeda.yapper.features.user.domain.repository.UserRepository
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUser @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(userId: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val user = userRepository.getUser(userId).toUser()
            emit(Resource.Success(user))
        } catch (e: RestException) {
            emit(Resource.Error(e.error))
        } catch (e: HttpRequestTimeoutException) {
            emit(Resource.Error("The request timed out. Try again later"))
        } catch (e: HttpRequestException) {
            emit(Resource.Error("Unable to connect to the server. Try again later"))
        }
    }
}