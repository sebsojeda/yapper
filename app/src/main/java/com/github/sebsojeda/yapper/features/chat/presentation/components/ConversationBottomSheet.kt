package com.github.sebsojeda.yapper.features.chat.presentation.components

import Avatar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.features.chat.domain.model.Conversation
import com.github.sebsojeda.yapper.ui.theme.Colors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationBottomSheet(
    conversation: Conversation,
    onClose: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var newConversationName by remember { mutableStateOf(conversation.name) }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Conversation Details",
                color = Colors.Neutral950,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Avatar(
                imageUrl = conversation.media?.path,
                displayName = conversation.name,
                size = 64)
            Box {
                TextField(
                    value = newConversationName ?: conversation.name,
                    onValueChange = { newConversationName = it },
                    modifier = Modifier
                        .align(Alignment.Center),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Colors.Transparent,
                        focusedContainerColor = Colors.Transparent,
                        unfocusedIndicatorColor = Colors.Transparent,
                        focusedIndicatorColor = Colors.Transparent,
                        disabledContainerColor = Colors.Transparent,
                        disabledIndicatorColor = Colors.Transparent,
                        cursorColor = Colors.Neutral950
                    ),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
        ) {
            TextButton(onClick = {
                newConversationName = conversation.name
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onClose()
                    }
                }
            }) {
                Text("Cancel", color = Colors.Neutral950)
            }
            TextButton(onClick = {

            }) {
                Text("Save", color = Colors.Indigo500)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConversationBottomSheetPreview() {
    ConversationBottomSheet(
        conversation = Conversation(
            id = "1",
            name = "Example Conversation",
            mediaId = null,
            media = null,
            participants = listOf(),
            messages = listOf(),
            createdAt = "2021-10-10T00:00:00Z",
        ),
        onClose = {}
    )
}