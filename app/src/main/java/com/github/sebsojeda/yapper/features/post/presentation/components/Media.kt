package com.github.sebsojeda.yapper.features.post.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.github.sebsojeda.yapper.core.Constants.BUCKET_PUBLIC_MEDIA
import com.github.sebsojeda.yapper.features.post.domain.model.PostMedia
import io.github.jan.supabase.storage.publicStorageItem

@Composable
fun Media(postMedia: List<PostMedia>) {
    if (postMedia.isEmpty()) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
    ) {
        when (postMedia.size) {
            1 -> {
//                "https://iowfhxbtfodcesrpfwpo.supabase.co/storage/v1/object/public/media/57a26f49-6182-492a-a629-f04e8f560be0"
                AsyncImage(
                    model = publicStorageItem(BUCKET_PUBLIC_MEDIA, postMedia[0].media.path),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = ColorPainter(MaterialTheme.colorScheme.outline),
                )
            }
            2 -> {
                AsyncImage(
                    model = publicStorageItem(BUCKET_PUBLIC_MEDIA, postMedia[0].media.path),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.weight(1f),
                    placeholder = ColorPainter(MaterialTheme.colorScheme.outline)
                )
                Spacer(modifier = Modifier.width(1.dp))
                AsyncImage(
                    model = publicStorageItem(BUCKET_PUBLIC_MEDIA, postMedia[1].media.path),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.weight(1f),
                    placeholder = ColorPainter(MaterialTheme.colorScheme.outline)
                )
            }
            3 -> {
                AsyncImage(
                    model = publicStorageItem(BUCKET_PUBLIC_MEDIA, postMedia[0].media.path),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.weight(1f),
                    placeholder = ColorPainter(MaterialTheme.colorScheme.outline)
                )
                Spacer(modifier = Modifier.width(1.dp))
                Column(modifier = Modifier.weight(1f)) {
                    AsyncImage(
                        model = publicStorageItem(BUCKET_PUBLIC_MEDIA, postMedia[1].media.path),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.weight(1f),
                        placeholder = ColorPainter(MaterialTheme.colorScheme.outline)
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    AsyncImage(
                        model = publicStorageItem(BUCKET_PUBLIC_MEDIA, postMedia[2].media.path),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.weight(1f),
                        placeholder = ColorPainter(MaterialTheme.colorScheme.outline)
                    )
                }
            }
            4 -> {
                Column(modifier = Modifier.weight(1f)) {
                    AsyncImage(
                        model = publicStorageItem(BUCKET_PUBLIC_MEDIA, postMedia[0].media.path),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.weight(1f),
                        placeholder = ColorPainter(MaterialTheme.colorScheme.outline)
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    AsyncImage(
                        model = publicStorageItem(BUCKET_PUBLIC_MEDIA, postMedia[1].media.path),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.weight(1f),
                        placeholder = ColorPainter(MaterialTheme.colorScheme.outline)
                    )
                }
                Spacer(modifier = Modifier.width(1.dp))
                Column(modifier = Modifier.weight(1f)) {
                    AsyncImage(
                        model = publicStorageItem(BUCKET_PUBLIC_MEDIA, postMedia[2].media.path),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.weight(1f),
                        placeholder = ColorPainter(MaterialTheme.colorScheme.outline)
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    AsyncImage(
                        model = publicStorageItem(BUCKET_PUBLIC_MEDIA, postMedia[3].media.path),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.weight(1f),
                        placeholder = ColorPainter(MaterialTheme.colorScheme.outline)
                    )
                }
            }
        }
    }
}
