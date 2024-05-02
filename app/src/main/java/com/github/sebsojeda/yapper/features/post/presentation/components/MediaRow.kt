package com.github.sebsojeda.yapper.features.post.presentation.components

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun MediaRow(media: List<Uri>, bucket: String? = null, shape: Shape = RoundedCornerShape(8.dp), onRemoveMedia: (Uri) -> Unit = {}, height: Dp = 200.dp) {
    if (media.isEmpty()) return
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
    ) {
        items(media) { uri ->
            Box {
                Image(uri = uri.toString(), bucket = bucket, modifier = Modifier
                    .height(height)
                    .clip(shape = shape))
                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = { onRemoveMedia(uri) }
                ) {
                    Icon(painter = painterResource(id = R.drawable.x_circle), contentDescription = null, tint = Colors.Neutral950)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MediaRowPreview() {
    MediaRow(
        media = listOf(
            Uri.parse("https://picsum.photos/200/300"),
            Uri.parse("https://picsum.photos/200/300")
        ),
    )
}