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
            authenticationRepository.signUp(email, password)
            val random = ('a'..'z').toList().shuffled().subList(0, 6).joinToString("")
            val username = "${name.split(" ")[0]}_${random}".lowercase()
            val user = CreateUserDto(username = username, name = name)
            userRepository.createUser(user)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }
}