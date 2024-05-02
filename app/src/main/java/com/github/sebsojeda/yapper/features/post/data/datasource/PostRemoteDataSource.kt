package com.github.sebsojeda.yapper.features.post.data.datasource

import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.post.data.dto.CreateCommentDto
import com.github.sebsojeda.yapper.features.post.data.dto.CreateLikeDto
import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetCommentDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetLikeDto
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
    suspend fun getComments(orderColumn: String, orderDescending: Boolean, limit: Long?): List<GetPostDto> =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POSTS)
                .select(Columns.raw("*, likes:likes(*), post_media:post_media(*, media:media(*)), user:user_id(*), post_reference:post_id(*, user:user_id(*), post_media:post_media(*, media:media(*)))")) {
                    order(orderColumn, if (orderDescending) Order.DESCENDING else Order.ASCENDING)
                    if (limit != null) limit(limit)
                }
                .decodeList<GetPostDto>()
        }

    suspend fun getComments(postId: String): List<GetCommentDto> =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POSTS)
                .select(Columns.raw("*, likes:likes(*), post_media:post_media(*, media:media(*)), user:user_id(*)")) {
                    filter {
                        eq("post_id", postId)
                    }
                }.decodeList<GetCommentDto>()
        }

    suspend fun getPost(postId: String): GetPostDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POSTS)
                .select(Columns.raw("*, likes:likes(*), post_media:post_media(*, media:media(*)), user:user_id(*), post_reference:post_id(*, user:user_id(*), post_media:post_media(*, media:media(*)))")) {
                    filter { eq("id", postId) }
                }.decodeSingle<GetPostDto>()
        }

    suspend fun createPost(post: CreatePostDto): GetPostDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POSTS)
                .insert(post) { select(Columns.raw("*, likes:likes(*), post_media:post_media(*, media:media(*)), user:user_id(*), post_reference:post_id(*, user:user_id(*), post_media:post_media(*, media:media(*)))")) }
                .decodeSingle<GetPostDto>()
        }

    suspend fun createComment(comment: CreateCommentDto): GetCommentDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POSTS)
                .insert(comment) { select(Columns.raw("*, likes:likes(*), post_media:post_media(*, media:media(*)), user:user_id(*)")) }
                .decodeSingle<GetCommentDto>()
        }

    suspend fun deletePost(postId: String): Unit =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POSTS)
                .delete { filter { eq("id", postId) } }
        }

    suspend fun likePost(like: CreateLikeDto): GetLikeDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_LIKES)
                .insert(like) { select() }
                .decodeSingle<GetLikeDto>()
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

    suspend fun searchPosts(query: String): List<GetPostDto> =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POSTS)
                .select(Columns.raw("*, likes:likes(*), post_media:post_media(*, media:media(*)), user:user_id(*), post_reference:post_id(*, user:user_id(*), post_media:post_media(*, media:media(*)))")) {
                    filter {
                        ilike("content", "%$query%")
                    }
                }.decodeList<GetPostDto>()
        }
}