package com.github.sebsojeda.yapper.features.chat.presentation.chat_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.LocalAuthContext
import com.github.sebsojeda.yapper.core.components.AppLayout
import com.github.sebsojeda.yapper.core.components.Loading
import com.github.sebsojeda.yapper.features.chat.domain.model.Conversation
import com.github.sebsojeda.yapper.features.chat.domain.model.Message
import com.github.sebsojeda.yapper.features.chat.domain.model.Participant
import com.github.sebsojeda.yapper.features.chat.presentation.ChatRoutes
import com.github.sebsojeda.yapper.features.chat.presentation.components.ChatCreateDialog
import com.github.sebsojeda.yapper.features.chat.presentation.components.ChatListItem
import com.github.sebsojeda.yapper.features.user.domain.model.User
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun ChatListScreen(
    state: ChatListState,
    currentRoute: String?,
    navigateTo: (String) -> Unit,
    createChat: (List<String>, String) -> Unit
) {
    val openCreateChatDialog = remember { mutableStateOf(false) }
    val auth = LocalAuthContext.current

    if (openCreateChatDialog.value) {
        ChatCreateDialog(
            isLoading = state.isChatLoading,
            onCancel = { openCreateChatDialog.value = false }
        ) { participants, content ->
            createChat(participants, content)
            openCreateChatDialog.value = false
        }
    }

    LaunchedEffect(state.chat) {
        if (state.chat != null) {
            navigateTo("${ChatRoutes.ChatDetail.route}/${state.chat.id}")
        }
    }

    AppLayout (
        title = { Text(text = "Messages") },
        currentRoute = currentRoute,
        navigateTo = navigateTo,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openCreateChatDialog.value = true },
                modifier = Modifier,
                shape = CircleShape,
                containerColor = Colors.Indigo500
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pencil_outline),
                    contentDescription = null,
                    tint = Colors.White
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                Loading(modifier = Modifier.align(Alignment.Center))
            } else if (state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    color = Colors.Red500,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.conversations.isEmpty()) {
                Text(text = "No conversations...yet",
                    modifier = Modifier.align(Alignment.Center),
                    color = Colors.Neutral400
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.conversations) { conversation ->
                        val participant = conversation.participants.first { it.userId != auth.user.id }
                        val participants = conversation.participants.filter { it.userId != auth.user.id }
                        val path = conversation.media?.path ?: participant.user.avatar?.path
                        val name = conversation.name ?: participants.joinToString { it.user.name }
                        val preview = conversation.messages.lastOrNull()?.content
                        ChatListItem(img = path, name = name, preview = preview) {
                            navigateTo("${ChatRoutes.ChatDetail.route}/${conversation.id}")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListScreenPreview() {
    ChatListScreen(
        state = ChatListState(
            conversations = listOf(
                Conversation(
                    id = "1",
                    name = "Sebastian",
                    participants = listOf(
                        Participant(
                            conversationId = "1",
                            userId = "1",
                            user = User(
                                id = "1",
                                name = "Sebastian",
                                username = "sebsojeda",
                                createdAt = "2021-10-10T00:00:00Z",
                                avatar = null
                            ),
                            createdAt = "2021-10-10T00:00:00Z"
                        ),
                        Participant(
                            conversationId = "1",
                            userId = "2",
                            user = User(
                                id = "2",
                                name = "John Doe",
                                username = "johndoe",
                                createdAt = "2021-10-10T00:00:00Z",
                                avatar = null
                            ),
                            createdAt = "2021-10-10T00:00:00Z"
                        )
                    ),
                    messages = listOf(
                        Message(
                            id = "1",
                            conversationId = "1",
                            content = "Hello",
                            createdAt = "2021-10-10T00:00:00Z",
                            userId = "1",
                            user = User(
                                id = "1",
                                name = "Sebastian",
                                username = "sebsojeda",
                                createdAt = "2021-10-10T00:00:00Z",
                                avatar = null
                            )
                        )
                    ),
                    mediaId = null,
                    media = null,
                    createdAt = "2021-10-10T00:00:00Z"
                )
            )
        ),
        currentRoute = ChatRoutes.ChatList.route,
        navigateTo = {},
        createChat = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun ChatListPreviewLoading() {
    ChatListScreen(
        state = ChatListState(
            isLoading = true
        ),
        currentRoute = ChatRoutes.ChatList.route,
        navigateTo = {},
        createChat = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun ChatListPreviewError() {
    ChatListScreen(
        state = ChatListState(
            error = "An unexpected error occurred"
        ),
        currentRoute = ChatRoutes.ChatList.route,
        navigateTo = {},
        createChat = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun ChatListPreviewEmpty() {
    ChatListScreen(
        state = ChatListState(
            conversations = emptyList()
        ),
        currentRoute = ChatRoutes.ChatList.route,
        navigateTo = {},
        createChat = { _, _ -> }
    )
}
