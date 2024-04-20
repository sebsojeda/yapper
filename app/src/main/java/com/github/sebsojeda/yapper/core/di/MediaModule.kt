package com.github.sebsojeda.yapper.core.di

import com.github.sebsojeda.yapper.core.data.datasource.MediaRemoteDataSource
import com.github.sebsojeda.yapper.core.data.datasource.MediaStorageRemoteDataSource
import com.github.sebsojeda.yapper.core.data.repository.MediaRepositoryImpl
import com.github.sebsojeda.yapper.core.data.repository.MediaStorageRepositoryImpl
import com.github.sebsojeda.yapper.core.domain.repository.MediaRepository
import com.github.sebsojeda.yapper.core.domain.repository.MediaStorageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MediaModule {
    @Provides
    @Singleton
    fun providesMediaStorageRepository(dataSource: MediaStorageRemoteDataSource): MediaStorageRepository {
        return MediaStorageRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun providesMediaRepository(dataSource: MediaRemoteDataSource): MediaRepository {
        return MediaRepositoryImpl(dataSource)
    }
}