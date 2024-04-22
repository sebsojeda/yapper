package com.github.sebsojeda.yapper.features.chat.presentation.chat_create

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.github.sebsojeda.yapper.features.chat.presentation.ChatRoutes

@Composable
fun ChatCreateScreen(
    navController: NavController,
    viewModel: ChatCreateViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val contentResolver = LocalContext.current.contentResolver
    val focusRequester = remember { FocusRequester() }
    var participants by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    if (state.value.isChatCreated) {
        navController.navigate(ChatRoutes.ChatDetail.route + "/${state.value.conversationId}")
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
                    val usernames = participants.split(",")
                        .map {
                            it.trim().removePrefix("@")
                        }
                    viewModel.createChat(usernames, content)
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                enabled = content.isNotBlank() && !state.value.isLoading && participants.isNotEmpty()
            ) {
                Text("Send")
            }
        }
        TextField(
            value = participants,
            onValueChange = { participants = it },
            prefix = { Text("To: ") },
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
        Column {
            TextField(
                value = content,
                onValueChange = { content = it },
                placeholder = { Text("Start a message") },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
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