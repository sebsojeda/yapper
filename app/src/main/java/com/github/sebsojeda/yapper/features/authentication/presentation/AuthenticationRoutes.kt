package com.github.sebsojeda.yapper.features.authentication.presentation

sealed class AuthenticationRoutes(val route: String) {
    data object Auth : AuthenticationRoutes("auth")
    data object SignIn : AuthenticationRoutes("sign_in")
    data object SignUp : AuthenticationRoutes("sign_up")
    data object Welcome : AuthenticationRoutes("welcome")
}