package com.github.sebsojeda.yapper.features.authentication.presentation.sign_in

data class SignInState(
    val isLoading: Boolean = false,
    val error: String = "",
)
