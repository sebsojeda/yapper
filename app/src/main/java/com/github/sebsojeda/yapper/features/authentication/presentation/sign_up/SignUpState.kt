package com.github.sebsojeda.yapper.features.authentication.presentation.sign_up

data class SignUpState(
    val isLoading: Boolean = false,
    val isPendingEmailConfirmation: Boolean = false,
    val error: String = "",
)
