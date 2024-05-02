package com.github.sebsojeda.yapper.features.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.github.sebsojeda.yapper.core.LocalAuthContext
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun ChatCreateDialog(
    isLoading: Boolean,
    onCancel: () -> Unit,
    onCreateChat: (List<String>, String) -> Unit
) {
    val auth = LocalAuthContext.current
    val focusRequester = remember { FocusRequester() }
    val participants = remember { mutableStateListOf<String>() }
    var participant by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = onCancel,
                    enabled = !isLoading,
                    modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text("Cancel", color = Colors.Neutral950)
                }
                Button(
                    onClick = { onCreateChat(participants, content) },
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Colors.Indigo500,
                        disabledContainerColor = Colors.Indigo300,
                        disabledContentColor = Colors.Indigo500,
                    ),
                    enabled = content.isNotBlank() && !isLoading && participants.isNotEmpty()
                ) {
                    Text("Send")
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("To: ", color = Colors.Neutral400, modifier = Modifier.padding(start = 16.dp))
                participants.forEach { participant ->
                    TextChip(participant, modifier = Modifier.padding(end = 4.dp)) {
                        participants.remove(participant)
                    }
                }
                TextField(
                    value = participant,
                    onValueChange = {
                        if (it.endsWith(" ")) {
                            val username = it.trim().removePrefix("@")
                            if (username != auth.user.username) {
                                participants.add(username)
                            }
                            participant = ""
                        } else {
                            participant = it
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (!it.isFocused && participant.isNotBlank()) {
                                val username = participant.trim().removePrefix("@")
                                if (username != auth.user.username) {
                                    participants.add(username)
                                }
                                participant = ""
                            }
                        }
                        .onKeyEvent {
                            if (it.key == Key.Backspace && participant.isEmpty() && participants.isNotEmpty()) {
                                participants.removeAt(participants.size - 1)
                                true
                            } else {
                                false
                            }
                        },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Colors.Transparent,
                        focusedContainerColor = Colors.Transparent,
                        unfocusedIndicatorColor = Colors.Transparent,
                        focusedIndicatorColor = Colors.Transparent,
                        cursorColor = Colors.Neutral950
                    ),
                )
            }
            Column {
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Start a message", color = Colors.Neutral400) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Colors.Transparent,
                        focusedContainerColor = Colors.Transparent,
                        unfocusedIndicatorColor = Colors.Transparent,
                        focusedIndicatorColor = Colors.Transparent,
                        cursorColor = Colors.Neutral950
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatCreateDialogPreview() {
    ChatCreateDialog(
        isLoading = false,
        onCancel = {},
        onCreateChat = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun ChatCreateDialogPreviewLoading() {
    ChatCreateDialog(
        isLoading = true,
        onCancel = {},
        onCreateChat = { _, _ -> }
    )
}