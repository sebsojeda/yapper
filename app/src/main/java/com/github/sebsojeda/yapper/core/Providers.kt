package com.github.sebsojeda.yapper.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.staticCompositionLocalOf
import com.github.sebsojeda.yapper.core.presentation.MainViewModel
import com.github.sebsojeda.yapper.features.user.domain.model.User

data class AuthContext(
    val user: User,
    val signOut: () -> Unit
)

val LocalAuthContext = staticCompositionLocalOf {
    AuthContext(
        user = User(
            id = "",
            name = "",
            username = "",
            createdAt = "",
            avatar = null
        ),
        signOut = {}
    )
}

@Composable
fun MainProviders(viewModel: MainViewModel, content: @Composable () -> Unit) {
    val user = viewModel.currentUser.collectAsState().value ?: return

    CompositionLocalProvider(
        LocalAuthContext provides AuthContext(
            user = user,
            signOut = { viewModel.signOut() }
        )
    ) {
        content()
    }
}