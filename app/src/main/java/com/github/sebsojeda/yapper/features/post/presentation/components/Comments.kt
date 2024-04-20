package com.github.sebsojeda.yapper.features.post.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.R

@Composable
fun Comments(comments: Int, onPostCommentClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(60.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.chat_bubble),
            contentDescription = "Comment",
            modifier = Modifier.clickable { onPostCommentClick() },
            tint = MaterialTheme.colorScheme.outline
        )
        Text(
            text = comments.toString(),
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommentsPreview() {
    Comments(10) {}
}