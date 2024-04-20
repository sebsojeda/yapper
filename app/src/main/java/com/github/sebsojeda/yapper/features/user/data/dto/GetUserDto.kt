package com.github.sebsojeda.yapper.features.user.data.dto

import com.github.sebsojeda.yapper.core.data.dto.GetMediaDto
import com.github.sebsojeda.yapper.core.data.dto.toMedia
import com.github.sebsojeda.yapper.features.user.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserDto(
    @SerialName("id") val id: String,
    @SerialName("username") val username: String,
    @SerialName("name") val name: String,
    @SerialName("avatar") val avatar: GetMediaDto?,
    @SerialName("created_at") val createdAt: String,
)

fun GetUserDto.toUser() = User(
    id = id,
    username = username,
    name = name,
    createdAt = createdAt,
    avatar = avatar?.toMedia(),
)