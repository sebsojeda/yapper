package com.github.sebsojeda.yapper.core.data.datasource

import com.github.sebsojeda.yapper.core.Constants
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaStorageRemoteDataSource @Inject constructor(
    private val dataSource: Storage,
) {
    suspend fun uploadMedia(path: String, data: ByteArray): Unit =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.BUCKET_PUBLIC_MEDIA)
                .upload(path, data, upsert = true)
        }

    suspend fun deleteMedia(path: String): Unit =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.BUCKET_PUBLIC_MEDIA)
                .delete(path)
        }
}