package com.github.sebsojeda.yapper.features.post.presentation.components

import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.sebsojeda.yapper.R
import java.io.File

@Composable
fun Camera(onImageCapture: (uri: Uri) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val file = FileProvider.getUriForFile(context, "com.github.sebsojeda.yapper.fileprovider", File.createTempFile("camera", ".jpg", context.cacheDir))
    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            onImageCapture(capturedImageUri)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            cameraLauncher.launch(capturedImageUri)
        }
    }

    Box(modifier = modifier) {
        IconButton(
            onClick = {
                val permissionCheckResult = ContextCompat.checkSelfPermission(context, "android.permission.CAMERA")
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(file)
                } else {
                    permissionLauncher.launch("android.permission.CAMERA")
                }
            }
        ) {
            Icon(painter = painterResource(id = R.drawable.camera_outline), contentDescription = null)
        }
    }
}