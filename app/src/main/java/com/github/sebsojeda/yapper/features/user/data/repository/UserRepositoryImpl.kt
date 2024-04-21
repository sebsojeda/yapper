package com.github.sebsojeda.yapper.features.user.data.repository

import com.github.sebsojeda.yapper.features.user.data.datasource.UserRemoteDataSource
import com.github.sebsojeda.yapper.features.user.data.dto.CreateUserDto
import com.github.sebsojeda.yapper.features.user.data.dto.GetUserDto
import com.github.sebsojeda.yapper.features.user.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
): UserRepository {
    override suspend fun getUser(userId: String): GetUserDto = userRemoteDataSource.getUser(userId)

    override suspend fun createUser(user: CreateUserDto): GetUserDto =
        userRemoteDataSource.createUser(user)

    override suspend fun updateUser(userId: String, user: CreateUserDto): GetUserDto =
        userRemoteDataSource.updateUser(userId, user)
}