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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.components.AppLayout
import com.github.sebsojeda.yapper.core.components.Loading
import com.github.sebsojeda.yapper.features.chat.domain.model.Conversation
import com.github.sebsojeda.yapper.features.chat.domain.model.Message
import com.github.sebsojeda.yapper.features.chat.domain.model.Participant
import com.github.sebsojeda.yapper.features.chat.presentation.ChatRoutes
import com.github.sebsojeda.yapper.features.chat.presentation.components.ChatListItem
import com.github.sebsojeda.yapper.features.user.domain.model.User
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun ChatListScreen(
    state: ChatListState,
    currentRoute: String?,
    navigateTo: (String) -> Unit,
) {
    AppLayout (
        title = { Text(text = "Messages") },
        currentRoute = currentRoute,
        navigateTo = navigateTo,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateTo(ChatRoutes.ChatCreate.route) },
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
            modifier = Modifier.fillMaxSize().padding(innerPadding)
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
                        val firstParticipant = conversation.participants?.firstOrNull()
                        val lastMessage = conversation.messages?.lastOrNull()
                        val path = conversation.media?.path ?: firstParticipant?.user?.avatar?.path
                        val name = conversation.name
                        val preview = lastMessage?.content
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
        navigateTo = {}
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
        navigateTo = {}
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
        navigateTo = {}
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
        navigateTo = {}
    )
}
