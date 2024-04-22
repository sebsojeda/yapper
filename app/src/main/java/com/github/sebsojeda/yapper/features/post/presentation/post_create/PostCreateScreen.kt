package com.github.sebsojeda.yapper.features.post.presentation.post_create

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.sebsojeda.yapper.core.domain.model.MediaUpload
import com.github.sebsojeda.yapper.features.post.presentation.PostRoutes
import com.github.sebsojeda.yapper.features.post.presentation.components.MediaLayout
import com.github.sebsojeda.yapper.features.post.presentation.components.MediaPicker
import com.github.sebsojeda.yapper.features.post.presentation.components.MediaPreview
import java.util.UUID

@Composable
fun PostCreateScreen(
    navController: NavController,
    viewModel: PostCreateViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val contentResolver = LocalContext.current.contentResolver
    val focusRequester = remember { FocusRequester() }
    var content by remember { mutableStateOf("") }
    var media by remember { mutableStateOf(emptyList<Uri>()) }

    if (state.value.isPostCreated) {
        navController.navigate(PostRoutes.PostList.route) {
            popUpTo(PostRoutes.PostList.route) { inclusive = false }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = {
                    navController.popBackStack()
                },
                enabled = !state.value.isLoading,
                modifier = Modifier.align(Alignment.CenterVertically)) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    val uploads = mutableListOf<MediaUpload>()
                    media.forEach { uri ->
                        contentResolver.openInputStream(uri)?.buffered()?.let {
                            val filePath = UUID.randomUUID().toString()
                            val data = it.readBytes()
                            val upload = MediaUpload(
                                data = data,
                                filePath = filePath,
                                fileSize = data.size
                            )
                            uploads.add(upload)
                        }
                    }
                    viewModel.createPost(content, uploads)
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                enabled = content.isNotBlank() && !state.value.isLoading
            ) {
                Text("Post")
            }
        }
        Column {
            TextField(
                value = content,
                onValueChange = { content = it },
                placeholder = { Text("What's happening?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                )
            )
            MediaPreview(media = media.map { it.toString() }, layout = MediaLayout.ROW)
            Row {
                MediaPicker(
                    modifier = Modifier,
                    onSelectMedia = { media = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
//                Camera(onImageCapture = { media = listOf(it) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostCreateScreenPreview() {
    PostCreateScreen(navController = rememberNavController())
}