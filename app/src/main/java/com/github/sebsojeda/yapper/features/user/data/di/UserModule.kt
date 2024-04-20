package com.github.sebsojeda.yapper.features.user.data.di

import com.github.sebsojeda.yapper.features.user.data.datasource.UserRemoteDataSource
import com.github.sebsojeda.yapper.features.user.data.repository.UserRepositoryImpl
import com.github.sebsojeda.yapper.features.user.domain.repository.UserRepository
import com.github.sebsojeda.yapper.features.user.domain.usecase.GetUser
import com.github.sebsojeda.yapper.features.user.domain.usecase.UserUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UserModule {
    @Provides
    @Singleton
    fun provideUserRepository(dataSource: UserRemoteDataSource): UserRepository {
        return UserRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun providesUserUseCases(userRepository: UserRepository): UserUseCases {
        return UserUseCases(
            getUser = GetUser(userRepository)
        )
    }
}