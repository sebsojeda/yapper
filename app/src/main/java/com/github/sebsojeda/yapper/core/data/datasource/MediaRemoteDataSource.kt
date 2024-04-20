package com.github.sebsojeda.yapper.core.data.datasource

import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.data.dto.CreateMediaDto
import com.github.sebsojeda.yapper.core.data.dto.GetMediaDto
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRemoteDataSource @Inject constructor(
    private val dataSource: Postgrest,
) {
    suspend fun createMedia(media: List<CreateMediaDto>): List<GetMediaDto> =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_POST_MEDIA)
                .insert(media) { select() }
                .decodeList<GetMediaDto>()
        }
}