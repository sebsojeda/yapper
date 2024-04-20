package com.github.sebsojeda.yapper.features.post.data.datasource

import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.post.data.dto.CreatePostMediaDto
import com.github.sebsojeda.yapper.features.post.data.dto.GetPostMediaDto
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostMediaRemoteDataSource @Inject constructor(
    private val dataSource: Postgrest,
) {
    suspend fun createPostMedia(postMedia: List<CreatePostMediaDto>): List<GetPostMediaDto> =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POST_MEDIA)
                .insert(postMedia) { select() }
                .decodeList<GetPostMediaDto>()
        }
}