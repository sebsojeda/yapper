package com.github.sebsojeda.yapper.features.post.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MediaGrid(media: List<String>, bucket: String?, shape: Shape = RoundedCornerShape(8.dp)) {
    if (media.isEmpty()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = shape)
    ) {
        when (media.size) {
            1 -> {
                Image(uri = media[0], bucket = bucket, modifier = Modifier.fillMaxWidth())
            }
            2 -> {
                Image(uri = media[0], bucket = bucket, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(1.dp))
                Image(uri = media[1], bucket = bucket, modifier = Modifier.weight(1f))
            }
            3 -> {
                Image(uri = media[0], bucket = bucket, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(1.dp))
                Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    Image(uri = media[1], bucket = bucket, modifier = Modifier.fillMaxWidth().weight(1f))
                    Spacer(modifier = Modifier.height(1.dp))
                    Image(uri = media[2], bucket = bucket, modifier = Modifier.fillMaxWidth().weight(1f))
                }
            }
            4 -> {
                Column(modifier = Modifier.weight(1f)) {
                    Image(uri = media[0], bucket = bucket, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(1.dp))
                    Image(uri = media[1], bucket = bucket, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.width(1.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Image(uri = media[2], bucket = bucket, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(1.dp))
                    Image(uri = media[3], bucket = bucket, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun MediaGridPreview1() {
    MediaGrid(
        media = listOf(
            "https://images.unsplash.com/photo-1632216820000-4b3b3b3b3b3b"
        ),
        bucket = null
    )
}
@Composable
@Preview(showBackground = true)
fun MediaGridPreview2() {
    MediaGrid(
        media = listOf(
            "https://images.unsplash.com/photo-1632216820000-4b3b3b3b3b3b",
            "https://images.unsplash.com/photo-1632216820000-4b3b3b3b3b3b"
        ),
        bucket = null
    )
}

@Composable
@Preview(showBackground = true)
fun MediaGridPreview3() {
    MediaGrid(
        media = listOf(
            "https://images.unsplash.com/photo-1632216820000-4b3b3b3b3b3b",
            "https://images.unsplash.com/photo-1632216820000-4b3b3b3b3b3b",
            "https://images.unsplash.com/photo-1632216820000-4b3b3b3b3b3b"
        ),
        bucket = null
    )
}

@Composable
@Preview(showBackground = true)
fun MediaGridPreview4() {
    MediaGrid(
        media = listOf(
            "https://images.unsplash.com/photo-1632216820000-4b3b3b3b3b3b",
            "https://images.unsplash.com/photo-1632216820000-4b3b3b3b3b3b",
            "https://images.unsplash.com/photo-1632216820000-4b3b3b3b3b3b",
            "https://images.unsplash.com/photo-1632216820000-4b3b3b3b3b3b"
        ),
        bucket = null
    )
}