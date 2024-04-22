package com.github.sebsojeda.yapper.features.post.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun MediaRow(media: List<String>, bucket: String?) {
    LazyRow(
        modifier = Modifier.padding(8.dp)
    ) {
        items(media) { uri ->
            Image(uri = uri, bucket = bucket, modifier = Modifier.height(200.dp).clip(shape = MaterialTheme.shapes.medium))
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}