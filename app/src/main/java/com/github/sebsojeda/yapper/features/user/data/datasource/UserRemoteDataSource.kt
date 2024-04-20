package com.github.sebsojeda.yapper.features.user.data.datasource

import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.user.data.dto.CreateUserDto
import com.github.sebsojeda.yapper.features.user.data.dto.GetUserDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val dataSource: Postgrest
){
    suspend fun getUser(userId: String): GetUserDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_USERS)
                .select(Columns.raw("*, avatar(*)")) {
                    filter { eq("id", userId) }
                }.decodeSingle<GetUserDto>()
        }

    suspend fun createUser(user: CreateUserDto): GetUserDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_USERS)
                .insert(user) { select(Columns.raw("*, avatar(*)")) }
                .decodeSingle<GetUserDto>()
        }

    suspend fun updateUser(userId: String, user: CreateUserDto): GetUserDto =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_USERS)
                .update(user) {
                    select(Columns.raw("*, avatar(*)"))
                    filter { eq("id", userId) }
                }
                .decodeSingle<GetUserDto>()
        }

    suspend fun deleteUser(userId: String): Unit =
        withContext(Dispatchers.IO) {
            dataSource.from(Constants.TABLE_USERS)
                .delete { filter { eq("id", userId) } }
        }
}