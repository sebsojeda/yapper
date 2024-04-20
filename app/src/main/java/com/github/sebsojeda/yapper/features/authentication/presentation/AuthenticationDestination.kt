package com.github.sebsojeda.yapper.features.authentication.presentation

sealed class AuthenticationDestination(val route: String) {
    data object SignIn : AuthenticationDestination("sign_in")
    data object SignUp : AuthenticationDestination("sign_up")
    data object SignUpConfirmation : AuthenticationDestination("sign_up_confirmation")
    data object Landing : AuthenticationDestination("landing")
}