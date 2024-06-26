package com.github.sebsojeda.yapper.features.post.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun Likes(likedByUser: Boolean, likes: Int, onPostLikeClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(60.dp),
    ) {
        Icon(
            painter = painterResource(id = if (likedByUser) R.drawable.heart_solid else R.drawable.heart_outline),
            contentDescription = "Like",
            modifier = Modifier.clickable { onPostLikeClick() },
            tint = if (likedByUser) Colors.Red500 else Colors.Neutral400
        )
        Text(
            text = likes.toString(),
            color = Colors.Neutral400
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LikesPreview() {
    Likes(false,10) {}
}
