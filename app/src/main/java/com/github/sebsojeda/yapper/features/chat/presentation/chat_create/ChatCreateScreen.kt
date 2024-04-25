package com.github.sebsojeda.yapper.features.chat.presentation.chat_create

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.sebsojeda.yapper.features.chat.presentation.ChatRoutes
import com.github.sebsojeda.yapper.features.chat.presentation.components.TextChip
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun ChatCreateScreen(
    navController: NavController,
    viewModel: ChatCreateViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var participant by remember { mutableStateOf("") }
    val participants = remember { mutableStateListOf<String>() }
    var content by remember { mutableStateOf("") }

    if (state.value.isChatCreated) {
        navController.navigate(ChatRoutes.ChatDetail.route + "/${state.value.conversationId}")
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier = Modifier) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
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
                Text("Cancel", color = Colors.Neutral950)
            }
            Button(
                onClick = {
                    viewModel.createChat(participants, content)
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Colors.Indigo500,
                    disabledContainerColor = Colors.Indigo300,
                    disabledContentColor = Colors.Indigo500,
                ),
                enabled = content.isNotBlank() && !state.value.isLoading && participants.isNotEmpty()
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
                    participant = if (it.endsWith(" ")) {
                        participants.add(it.trim().removePrefix("@"))
                        ""
                    } else {
                        it
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (!it.isFocused && participant.isNotBlank()) {
                            participants.add(participant.trim().removePrefix("@"))
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
                )
            )
        }
    }

    if (state.value.error.isNotBlank()) {
        Log.e("ChatCreateScreen", state.value.error)
        Toast.makeText(LocalContext.current, state.value.error, Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun PostCreateScreenPreview() {
    ChatCreateScreen(navController = rememberNavController())
}