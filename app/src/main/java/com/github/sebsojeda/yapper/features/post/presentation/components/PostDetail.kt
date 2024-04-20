package com.github.sebsojeda.yapper.features.post.presentation.components

import Avatar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.core.domain.model.Media
import com.github.sebsojeda.yapper.features.post.domain.model.Post
import com.github.sebsojeda.yapper.features.post.domain.model.PostMedia
import com.github.sebsojeda.yapper.features.user.domain.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PostDetail(
    post: Post,
    onPostLikeClick: (String) -> Unit,
    onPostCommentClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Avatar(user = post.user, size = 48)
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                Text(
                    modifier = Modifier,
                    fontWeight = FontWeight.Bold,
                    text = post.user.name
                )
                Text(
                    text = "@${post.user.username}",
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }
        Column {
            Text(
                overflow = TextOverflow.Ellipsis,
                text = post.content,
                modifier = Modifier.padding(bottom = 2.dp),
            )
            Media(postMedia = post.postMedia)
            Text(
                text = parseTimestamp(post.createdAt),
                color = MaterialTheme.colorScheme.outline,
                fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                modifier = Modifier.padding(top = 8.dp)
            )
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Likes(likes = post.likes, onPostLikeClick = { onPostLikeClick(post.id) })
                Comments(comments = post.comments, onPostCommentClick = { onPostCommentClick(post.id) })
            }
        }
    }
}

private fun parseTimestamp(timestamp: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSS]")
    val outputFormatter = DateTimeFormatter.ofPattern("h:mm a • M/d/yy")

    val dateTime = LocalDateTime.parse(timestamp, inputFormatter)

    return dateTime.format(outputFormatter)
}

@Composable
@Preview(showBackground = true)
fun PostDetailPreview() {
    PostDetail(
        post = Post(
            id = "1",
            userId = "1",
            user = User(
                id = "1",
                username = "johndoe",
                name = "John Doe",
                createdAt = "",
                avatar = null,
            ),
            postId = "1",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud.",
            likes = 45,
            comments = 100,
            createdAt = "2021-01-01T00:00:00Z",
            postMedia = listOf(
                PostMedia(
                    postId = "1",
                    mediaId = "1",
                    media = Media(id = "1", path = "")
                ),
            ),
        ),
        onPostLikeClick = {},
        onPostCommentClick = {},
    )
}
