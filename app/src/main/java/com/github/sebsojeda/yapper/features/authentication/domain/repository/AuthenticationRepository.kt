package com.github.sebsojeda.yapper.features.authentication.domain.repository

import io.github.jan.supabase.gotrue.providers.builtin.Email

interface AuthenticationRepository {
    suspend fun signIn(email: String, password: String)
    suspend fun signUp(email: String, password: String): Email.Result?
    suspend fun resendEmailConfirmation(email: String)
    suspend fun signOut()
}
