package com.github.sebsojeda.yapper.features.authentication.domain.usecase

data class AuthenticationUseCases(
    val signIn: SignIn,
    val signUp: SignUp,
    val signUpConfirmation: SignUpConfirmation,
    val signOut: SignOut,
)
