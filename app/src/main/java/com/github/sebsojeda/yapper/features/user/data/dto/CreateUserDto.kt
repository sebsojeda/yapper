package com.github.sebsojeda.yapper.features.user.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserDto(
    @SerialName("username") val username: String,
    @SerialName("name") val name: String,
)
