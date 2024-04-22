package com.github.sebsojeda.yapper.features.authentication.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.authentication.presentation.sign_in.SignInScreen
import com.github.sebsojeda.yapper.features.authentication.presentation.sign_up.SignUpScreen
import com.github.sebsojeda.yapper.features.authentication.presentation.sign_up_confirmation.SignUpConfirmationScreen
import com.github.sebsojeda.yapper.features.authentication.presentation.welcome.WelcomeScreen

fun NavGraphBuilder.authGraph(navController: NavHostController) {
   navigation(
        startDestination = AuthenticationRoutes.Welcome.route,
        route = AuthenticationRoutes.Auth.route
    ) {
        composable(
            route = AuthenticationRoutes.Welcome.route
        ) {
            WelcomeScreen(navController)
        }
        composable(
            route = AuthenticationRoutes.SignUp.route
        ) {
            SignUpScreen(navController)
        }
        composable(
            route = AuthenticationRoutes.SignUpConfirmation.route + "/{${Constants.PARAM_EMAIL}}",
            arguments = listOf(
                navArgument(Constants.PARAM_EMAIL) {
                    type = NavType.StringType
                }
            )
        ) {
            SignUpConfirmationScreen(navController)
        }
        composable(
            route = AuthenticationRoutes.SignIn.route
        ) {
            SignInScreen(navController)
        }
    }
}