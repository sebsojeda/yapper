package com.github.sebsojeda.yapper.core.components

import Avatar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.features.user.domain.model.User
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun AppDrawer(user: User?, onSignOutClick: () -> Unit) {
    ModalDrawerSheet {
        Column(Modifier.padding(16.dp)) {
            if (user != null) {
                Avatar(imageUrl = user.avatar?.path, displayName = user.name, size = 32)
                Text(
                    text = user.name,
                    fontWeight = FontWeight.Bold,
                    color = Colors.Neutral950,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "@${user.username}",
                    color = Colors.Neutral400,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            TextButton(
                onClick = onSignOutClick,
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.textButtonColors(contentColor = Colors.Indigo500)
            ) {
                Text(text = "Sign Out")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppDrawerPreview() {
    AppDrawer(
        user = User(
            id = "1",
            name = "John Doe",
            username = "johndoe",
            avatar = null,
            createdAt = "2021-01-01T00:00:00Z",
        ),
        onSignOutClick = {}
    )
}