package com.github.sebsojeda.yapper.features.post.presentation.components

import Avatar
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.github.sebsojeda.yapper.features.post.domain.model.PostReference
import com.github.sebsojeda.yapper.features.user.domain.model.User
import com.github.sebsojeda.yapper.ui.theme.Colors
import kotlinx.datetime.toInstant

@Composable
fun PostPreview(
    post: PostReference,
    onPostClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPostClick(post.id) }
            .padding(top = 8.dp)
            .border(1.dp, Colors.Neutral200, RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Avatar(path = post.user.avatar?.path, name = post.user.name, size = 24)
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.padding(end = 2.dp),
                        fontWeight = FontWeight.Bold,
                        color = Colors.Neutral950,
                        text = post.user.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "@${post.user.username}",
                        color = Colors.Neutral400,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        modifier = Modifier,
                        text = " • " + TimeAgo.using(
                            post.createdAt.toInstant().toEpochMilliseconds()
                        ),
                        color = Colors.Neutral400,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    maxLines = 7,
                    overflow = TextOverflow.Ellipsis,
                    text = post.content,
                    modifier = Modifier.padding(bottom = 2.dp),
                )
            }
        }
        MediaGrid(
            media = post.postMedia.map { it.media.path },
            bucket = Constants.BUCKET_PUBLIC_MEDIA,
            shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PostPreviewPreview() {
    PostPreview(
        post = PostReference(
            id = "1",
            userId = "1",
            user = User(
                id = "1",
                name = "John Doe",
                username = "johndoe",
                avatar = null,
                createdAt = "2022-01-01T00:00:00Z",
            ),
            content = "This is a post",
            createdAt = "2022-01-01T00:00:00Z",
            postMedia = emptyList(),
        ),
        onPostClick = {},
    )
}