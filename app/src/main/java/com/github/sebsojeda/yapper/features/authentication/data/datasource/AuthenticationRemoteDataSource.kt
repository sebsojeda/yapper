package com.github.sebsojeda.yapper.features.authentication.data.datasource

import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationRemoteDataSource @Inject constructor(
    private val dataSource: Auth,
) {
    suspend fun signIn(email: String, password: String): Unit =
        withContext(Dispatchers.IO) {
            dataSource.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }

    suspend fun signUp(email: String, password: String): Email.Result? =
        withContext(Dispatchers.IO) {
            dataSource.signUpWith(Email) {
                this.email = email
                this.password = password
            }
        }

    suspend fun resendEmailConfirmation(email: String): Unit =
        withContext(Dispatchers.IO) {
            dataSource.resendEmail(email = email, type = OtpType.Email.EMAIL)
        }

    suspend fun signOut(): Unit =
        withContext(Dispatchers.IO) {
            dataSource.signOut()
        }
}