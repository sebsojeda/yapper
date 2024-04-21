package com.github.sebsojeda.yapper.features.authentication.domain.usecase

data class AuthenticationUseCases(
    val signIn: SignIn,
    val signUp: SignUp,
    val resendEmail: ResendEmail,
    val confirmEmail: ConfirmEmail,
)
