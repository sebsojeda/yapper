package com.github.sebsojeda.yapper.features.post.di

import com.github.sebsojeda.yapper.core.domain.repository.MediaRepository
import com.github.sebsojeda.yapper.core.domain.repository.MediaStorageRepository
import com.github.sebsojeda.yapper.features.post.data.PostManagerImpl
import com.github.sebsojeda.yapper.features.post.data.datasource.PostMediaRemoteDataSource
import com.github.sebsojeda.yapper.features.post.data.datasource.PostRemoteDataSource
import com.github.sebsojeda.yapper.features.post.data.repository.PostMediaRepositoryImpl
import com.github.sebsojeda.yapper.features.post.data.repository.PostRepositoryImpl
import com.github.sebsojeda.yapper.features.post.domain.repository.PostManager
import com.github.sebsojeda.yapper.features.post.domain.repository.PostMediaRepository
import com.github.sebsojeda.yapper.features.post.domain.repository.PostRepository
import com.github.sebsojeda.yapper.features.post.domain.usecase.CreatePost
import com.github.sebsojeda.yapper.features.post.domain.usecase.GetPost
import com.github.sebsojeda.yapper.features.post.domain.usecase.GetPostComments
import com.github.sebsojeda.yapper.features.post.domain.usecase.GetPosts
import com.github.sebsojeda.yapper.features.post.domain.usecase.LikePost
import com.github.sebsojeda.yapper.features.post.domain.usecase.PostUseCases
import com.github.sebsojeda.yapper.features.post.domain.usecase.SearchPosts
import com.github.sebsojeda.yapper.features.post.domain.usecase.UnlikePost
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.gotrue.Auth
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PostModule {
    @Provides
    @Singleton
    fun providePostMediaRepository(dataSource: PostMediaRemoteDataSource): PostMediaRepository {
        return PostMediaRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun providesPostRepository(dataSource: PostRemoteDataSource): PostRepository {
        return PostRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun providesPostManager(
        postRepository: PostRepository,
        mediaRepository: MediaRepository,
        postMediaRepository: PostMediaRepository,
        mediaStorageRepository: MediaStorageRepository,
        auth: Auth,
    ): PostManager {
        return PostManagerImpl(
            postRepository,
            mediaRepository,
            postMediaRepository,
            mediaStorageRepository,
            auth
        )
    }

    @Provides
    @Singleton
    fun providesPostUseCases(postManager: PostManager): PostUseCases {
        return PostUseCases(
            createPost = CreatePost(postManager),
            getPost = GetPost(postManager),
            getPosts = GetPosts(postManager),
            getPostComments = GetPostComments(postManager),
            likePost = LikePost(postManager),
            unlikePost = UnlikePost(postManager),
            searchPosts = SearchPosts(postManager)
        )
    }
}