package com.github.sebsojeda.yapper.features.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.features.user.domain.model.User
import com.github.sebsojeda.yapper.ui.theme.Colors
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ChatBubble(
    message: String,
    align: Alignment.Horizontal,
    color: Color, timestamp: String,
    user: User,
    showUser: Boolean = false
) {
    val clip = if (align == Alignment.Start) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        if (showUser) {
            Row(
                modifier = Modifier.align(align)
            ) {
                Text(
                    text = user.name,
                    color = Colors.Neutral400,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Row(
            modifier = Modifier
                .align(align)
                .clip(clip)
        ) {
            Text(
                text = message,
                color = Colors.White,
                modifier = Modifier
                    .background(color = color)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Row(
            modifier = Modifier.align(align)
        ) {
            Text(
                text = parseTimestamp(timestamp),
                color = Colors.Neutral400,
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun parseTimestamp(timestamp: String): String {
    val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("h:mm a")

    val dateTime = ZonedDateTime.parse(timestamp, inputFormatter)
    val localDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())

    return localDateTime.format(outputFormatter)
}

@Preview(showBackground = true)
@Composable
fun ChatBubblePreviewEnd() {
    ChatBubble(
        message = "Hello, World!",
        align = Alignment.End,
        color = Colors.Indigo500,
        timestamp = "2021-08-01T12:00:00Z",
        user = User(
            id = "1",
            name = "Sebastian",
            username = "sebsojeda",
            createdAt = "2021-10-10T00:00:00Z",
            avatar = null
        ),
        showUser = true
    )
}

@Preview(showBackground = true)
@Composable
fun ChatBubblePreviewStart() {
    ChatBubble(
        message = "Hello, World!",
        align = Alignment.Start,
        color = Colors.Neutral400,
        timestamp = "2021-08-01T12:00:00Z",
        user = User(
            id = "1",
            name = "Sebastian",
            username = "sebsojeda",
            createdAt = "2021-10-10T00:00:00Z",
            avatar = null
        ),
        showUser = false
    )
}