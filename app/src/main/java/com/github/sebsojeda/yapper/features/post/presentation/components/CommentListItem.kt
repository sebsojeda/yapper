package com.github.sebsojeda.yapper.features.post.presentation.components

import Avatar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.features.post.domain.model.Comment
import com.github.sebsojeda.yapper.features.user.domain.model.User
import com.github.sebsojeda.yapper.ui.theme.Colors
import kotlinx.datetime.toInstant

@Composable
fun CommentListItem(
    comment: Comment,
    onPostClick: (String) -> Unit,
    onPostLikeClick: () -> Unit,
    onPostCommentClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPostClick(comment.id) }
            .topBorder(1.dp, Colors.Neutral200)
            .padding(8.dp)
    ) {
        Avatar(path = comment.user.avatar?.path, name = comment.user.name, size = 48)
        Spacer(modifier = Modifier.padding(4.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(end = 2.dp),
                    fontWeight = FontWeight.Bold,
                    color = Colors.Neutral950,
                    text = comment.user.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "@${comment.user.username}",
                    color = Colors.Neutral400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    modifier = Modifier,
                    text = " • " + TimeAgo.using(comment.createdAt.toInstant().toEpochMilliseconds()),
                    color = Colors.Neutral400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                maxLines = 7,
                overflow = TextOverflow.Ellipsis,
                text = comment.content,
                modifier = Modifier.padding(bottom = 2.dp),
            )
            MediaGrid(media = comment.postMedia.map { it.media.path }, bucket = Constants.BUCKET_PUBLIC_MEDIA)
            Row(modifier = Modifier.padding(top = 2.dp)) {
                Likes(likedByUser = comment.likedByUser, likes = comment.likes, onPostLikeClick = { onPostLikeClick() })
                Comments(comments = comment.comments, onPostCommentClick = { onPostCommentClick(comment.id) })
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CommentListItemPreview() {
    CommentListItem(
        comment = Comment(
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
            likedByUser = false,
            postMedia = emptyList(),
        ),
        onPostClick = {},
        onPostLikeClick = {},
        onPostCommentClick = {},
    )
}
