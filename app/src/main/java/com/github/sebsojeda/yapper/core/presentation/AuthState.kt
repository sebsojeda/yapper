package com.github.sebsojeda.yapper.core.presentation

import com.github.sebsojeda.yapper.features.user.domain.model.User

data class AuthState(
    val isAuthenticated: Boolean = false,
    val currentUser: User? = null,
)