package com.github.sebsojeda.yapper.features.authentication.presentation.sign_up_confirmation

data class SignUpConfirmationState(
    val isLoading: Boolean = false,
    val email: String = "",
    val error: String = "",
)
