package com.github.sebsojeda.yapper.features.post.presentation.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.github.sebsojeda.yapper.core.domain.model.MediaUpload
import com.github.sebsojeda.yapper.ui.theme.Colors
import java.util.UUID

@Composable
fun PostCreateDialog(
    onCancel: () -> Unit,
    onCreatePost: (String, List<MediaUpload>) -> Unit
) {
    val contentResolver = LocalContext.current.contentResolver
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    var content by remember { mutableStateOf("") }
    var media by remember { mutableStateOf(emptyList<Uri>()) }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0f)

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onCancel) {
                    Text("Cancel", color = Colors.Neutral950)
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
                        onCreatePost(content, uploads)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Colors.Indigo500,
                        disabledContainerColor = Colors.Indigo300,
                        disabledContentColor = Colors.Indigo500,
                    ),
                    enabled = content.isNotBlank()
                ) {
                    Text("Post")
                }
            }
            Column {
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("What's happening?", color = Colors.Neutral400) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { hasFocus = it.isFocused }
                        .weight(1f),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Colors.Transparent,
                        focusedContainerColor = Colors.Transparent,
                        unfocusedIndicatorColor = Colors.Transparent,
                        focusedIndicatorColor = Colors.Transparent,
                        cursorColor = Colors.Neutral950
                    ),
                )
                MediaRow(media = media, onRemoveMedia = { media = media - it })
                if (hasFocus) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .imePadding()
                    ) {
                        MediaPicker(
                            modifier = Modifier,
                            onSelectMedia = { media = it }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostCreateDialogPreview() {
    PostCreateDialog(
        onCancel = {},
        onCreatePost = { _, _ -> }
    )
}