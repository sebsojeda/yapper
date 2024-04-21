package com.github.sebsojeda.yapper.features.post.data.datasource

import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetCreateLikeDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetPostDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRemoteDataSource @Inject constructor(
    private val dataSource: Postgrest,
) {
    suspend fun getPosts(): List<GetPostDto> =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POSTS)
                .select(Columns.raw("*, likes:likes(*), post_media:post_media(*, media:media(*)), user:user_id(*)")) {
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<GetPostDto>()
        }

    suspend fun getPosts(postId: String): List<GetPostDto> =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POSTS)
                .select(Columns.raw("*, likes:likes(*), post_media:post_media(*, media:media(*)), user:user_id(*)")) {
                    filter {
                        eq("post_id", postId)
                    }
                }.decodeList<GetPostDto>()
        }

    suspend fun getPost(postId: String): GetPostDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POSTS)
                .select(Columns.raw("*, likes:likes(*), post_media:post_media(*, media:media(*)), user:user_id(*)")) {
                    filter { eq("id", postId) }
                }.decodeSingle<GetPostDto>()
        }

    suspend fun createPost(post: CreatePostDto): GetPostDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POSTS)
                .insert(post) { select(Columns.raw("*, likes:likes(*), post_media:post_media(*, media:media(*)), user:user_id(*)")) }
                .decodeSingle<GetPostDto>()
        }

    suspend fun deletePost(postId: String): Unit =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POSTS)
                .delete { filter { eq("id", postId) } }
        }

    suspend fun likePost(like: GetCreateLikeDto): GetCreateLikeDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_LIKES)
                .insert(like) { select() }
                .decodeSingle<GetCreateLikeDto>()
        }

    suspend fun unlikePost(postId: String, userId: String): Unit =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_LIKES)
                .delete {
                    filter {
                        eq("post_id", postId)
                        eq("user_id", userId)
                    }
                }
        }
}