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
fun Likes(likes: Int, onPostLikeClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(60.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.heart_outline),
            contentDescription = "Like",
            modifier = Modifier.clickable { onPostLikeClick() },
            tint = MaterialTheme.colorScheme.outline
        )
        Text(
            text = likes.toString(),
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LikesPreview() {
    Likes(10) {}
}
