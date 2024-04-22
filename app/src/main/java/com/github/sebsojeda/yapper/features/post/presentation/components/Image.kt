package com.github.sebsojeda.yapper.features.post.presentation.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.github.sebsojeda.yapper.core.Constants

@Composable
fun Image(uri: String, modifier: Modifier = Modifier, bucket: String? = null) {
    val model: Any = if (bucket != null) {
        "${Constants.SUPABASE_URL}/storage/v1/object/public/$bucket/$uri"
    } else {
        uri
    }
    AsyncImage(
        model = model,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier,
        placeholder = ColorPainter(Color.LightGray),
        onError = { error ->
            Log.e("Image", "Error loading image: $error")
        },
        onSuccess = { image ->
            Log.d("Image", "Image loaded: ${image.result.request}")
        },
        fallback = ColorPainter(Color.LightGray)
    )
}