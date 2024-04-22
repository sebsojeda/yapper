package com.github.sebsojeda.yapper.features.user.domain.repository

import com.github.sebsojeda.yapper.features.user.data.dto.CreateUserDto
import com.github.sebsojeda.yapper.features.user.data.dto.GetUserDto

interface UserRepository {
    suspend fun getUser(userId: String): GetUserDto

    suspend fun getUserByUsername(username: String): GetUserDto

    suspend fun createUser(user: CreateUserDto): GetUserDto

    suspend fun updateUser(userId: String, user: CreateUserDto): GetUserDto
}