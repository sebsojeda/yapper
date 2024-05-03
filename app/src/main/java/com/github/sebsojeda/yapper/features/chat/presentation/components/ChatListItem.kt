package com.github.sebsojeda.yapper.features.chat.presentation.components

import Avatar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChatListItem(img: String?, name: String, preview: String?, onChatClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onChatClick() }
    ) {
        Avatar(
            imageUrl = img,
            displayName = name,
            size = 48
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Column {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = preview ?: "No messages yet",
                fontStyle = if (preview != null) FontStyle.Normal else FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListItemPreview() {
    ChatListItem(
        img = null,
        name = "John Doe",
        preview = "Hello, how are you?",
        onChatClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ChatListItemPreviewNoPreview() {
    ChatListItem(
        img = null,
        name = "John Doe",
        preview = null,
        onChatClick = {}
    )
}