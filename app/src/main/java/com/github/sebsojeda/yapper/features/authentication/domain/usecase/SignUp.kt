package com.github.sebsojeda.yapper.features.authentication.domain.usecase

import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.authentication.domain.repository.AuthenticationRepository
import com.github.sebsojeda.yapper.features.user.data.dto.CreateUserDto
import com.github.sebsojeda.yapper.features.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUp @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(name: String, email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val result = authenticationRepository.signUp(email, password)
            if (result == null) {
                emit(Resource.Error("An error occurred"))
                return@flow
            }
            val username = "${name.split(" ")[0]}_${result.createdAt.epochSeconds}"
            val user = CreateUserDto(result.id, username = username, name = name)
            userRepository.createUser(user)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }
}