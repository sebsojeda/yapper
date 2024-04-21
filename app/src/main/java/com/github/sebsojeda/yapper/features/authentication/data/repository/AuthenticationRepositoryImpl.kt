package com.github.sebsojeda.yapper.features.authentication.data.repository

import com.github.sebsojeda.yapper.features.authentication.data.datasource.AuthenticationRemoteDataSource
import com.github.sebsojeda.yapper.features.authentication.domain.repository.AuthenticationRepository
import io.github.jan.supabase.gotrue.providers.builtin.Email
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val dataSource: AuthenticationRemoteDataSource,
) : AuthenticationRepository {
    override suspend fun signIn(email: String, password: String): Unit =
        dataSource.signIn(email, password)

    override suspend fun signUp(email: String, password: String): Email.Result? =
        dataSource.signUp(email, password)

    override suspend fun resendEmailConfirmation(email: String): Unit =
        dataSource.resendEmailConfirmation(email)

    override suspend fun confirmEmail(email: String, token: String): Unit =
        dataSource.confirmEmail(email, token)
}