package com.github.sebsojeda.yapper.core

import androidx.compose.runtime.staticCompositionLocalOf
import com.github.sebsojeda.yapper.features.user.domain.model.User

data class AuthContext(
    val user: User,
    val signOut: () -> Unit
)

val LocalAuthContext = staticCompositionLocalOf {
    AuthContext(
        user = User(
            id = "1",
            name = "Default",
            username = "default",
            createdAt = "2021-01-01T00:00:00Z",
            avatar = null
        ),
        signOut = {}
    )
}
