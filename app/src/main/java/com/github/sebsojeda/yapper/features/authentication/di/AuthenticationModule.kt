package com.github.sebsojeda.yapper.features.authentication.di

import com.github.sebsojeda.yapper.features.authentication.data.datasource.AuthenticationRemoteDataSource
import com.github.sebsojeda.yapper.features.authentication.data.repository.AuthenticationRepositoryImpl
import com.github.sebsojeda.yapper.features.authentication.domain.repository.AuthenticationRepository
import com.github.sebsojeda.yapper.features.authentication.domain.usecase.AuthenticationUseCases
import com.github.sebsojeda.yapper.features.authentication.domain.usecase.SignIn
import com.github.sebsojeda.yapper.features.authentication.domain.usecase.SignOut
import com.github.sebsojeda.yapper.features.authentication.domain.usecase.SignUp
import com.github.sebsojeda.yapper.features.authentication.domain.usecase.SignUpConfirmation
import com.github.sebsojeda.yapper.features.user.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AuthenticationModule {
    @Provides
    @Singleton
    fun provideAuthenticationRepository(dataSource: AuthenticationRemoteDataSource): AuthenticationRepository {
        return AuthenticationRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideAuthenticationUseCases(
        authenticationRepository: AuthenticationRepository,
        userRepository: UserRepository,
    ): AuthenticationUseCases {
        return AuthenticationUseCases(
            signIn = SignIn(authenticationRepository),
            signUp = SignUp(authenticationRepository, userRepository),
            signUpConfirmation = SignUpConfirmation(authenticationRepository),
            signOut = SignOut(authenticationRepository)
        )
    }
}