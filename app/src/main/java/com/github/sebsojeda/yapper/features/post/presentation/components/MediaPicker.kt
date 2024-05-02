package com.github.sebsojeda.yapper.features.post.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun MediaPicker(modifier: Modifier = Modifier, onSelectMedia: (List<Uri>) -> Unit) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(Constants.POST_MAX_MEDIA)) {
        onSelectMedia(it)
    }

    Box(modifier = modifier) {
        IconButton(onClick = {
            launcher.launch(
                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Icon(painter = painterResource(id = R.drawable.photo_outline), contentDescription = null, tint = Colors.Indigo500)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MediaPickerPreview() {
    MediaPicker {}
}