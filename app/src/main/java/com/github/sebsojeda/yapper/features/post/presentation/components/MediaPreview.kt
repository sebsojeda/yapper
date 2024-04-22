package com.github.sebsojeda.yapper.features.post.presentation.components

import androidx.compose.runtime.Composable

enum class MediaLayout {
    GRID, ROW
}

@Composable
fun MediaPreview(media: List<String>, bucket: String? = null, layout: MediaLayout = MediaLayout.GRID) {
    if (media.isEmpty()) return

    when (layout) {
        MediaLayout.GRID -> {
            MediaGrid(media = media, bucket = bucket)
        }
        MediaLayout.ROW -> {
            MediaRow(media = media, bucket = bucket)
        }
    }


}
