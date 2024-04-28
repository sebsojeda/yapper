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
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.domain.model.Media
import com.github.sebsojeda.yapper.features.post.domain.model.Post
import com.github.sebsojeda.yapper.features.post.domain.model.PostMedia
import com.github.sebsojeda.yapper.features.user.domain.model.User
import com.github.sebsojeda.yapper.ui.theme.Colors
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PostDetail(
    post: Post,
    onPostLikeClick: () -> Unit,
    onPostCommentClick: (String) -> Unit,
    onPostReferenceClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Avatar(imageUrl = post.user.avatar?.path, displayName = post.user.name, size = 48)
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                Text(
                    modifier = Modifier,
                    fontWeight = FontWeight.Bold,
                    text = post.user.name,
                )
                Text(
                    text = "@${post.user.username}",
                    color = Colors.Neutral400,
                )
            }
        }
        Column {
            Text(
                overflow = TextOverflow.Ellipsis,
                text = post.content,
                modifier = Modifier.padding(bottom = 2.dp),
            )
            MediaGrid(media = post.postMedia.map { it.media.path }, bucket = Constants.BUCKET_PUBLIC_MEDIA)
            if (post.postReference != null) {
                PostPreview(post = post.postReference, onPostClick = {
                    onPostReferenceClick(post.postReference.id)
                })
            }
            Text(
                text = parseTimestamp(post.createdAt),
                color = Colors.Neutral400,
                fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                modifier = Modifier.padding(top = 8.dp)
            )
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Likes(likedByUser = post.likedByUser, likes = post.likes, onPostLikeClick = { onPostLikeClick() })
                Comments(comments = post.comments, onPostCommentClick = { onPostCommentClick(post.id) })
            }
        }
    }
}

private fun parseTimestamp(timestamp: String): String {
    val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("h:mm a • M/d/yy")

    val dateTime = ZonedDateTime.parse(timestamp, inputFormatter)
    val localDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())

    return localDateTime.format(outputFormatter)
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
            likedByUser = false,
            createdAt = "2021-01-01T00:00:00Z",
            postMedia = listOf(
                PostMedia(
                    postId = "1",
                    mediaId = "1",
                    media = Media(id = "1", path = "")
                ),
            ),
            postReference = null,
        ),
        onPostLikeClick = {},
        onPostCommentClick = {},
        onPostReferenceClick = {},
    )
}
