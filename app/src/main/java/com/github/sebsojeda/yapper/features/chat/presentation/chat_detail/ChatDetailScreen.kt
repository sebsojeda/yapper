package com.github.sebsojeda.yapper.features.chat.presentation.chat_detail

import Avatar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.LocalAuthContext
import com.github.sebsojeda.yapper.core.components.AppLayout
import com.github.sebsojeda.yapper.core.components.Loading
import com.github.sebsojeda.yapper.features.chat.domain.model.Conversation
import com.github.sebsojeda.yapper.features.chat.domain.model.Message
import com.github.sebsojeda.yapper.features.chat.domain.model.Participant
import com.github.sebsojeda.yapper.features.chat.presentation.ChatRoutes
import com.github.sebsojeda.yapper.features.chat.presentation.components.ChatBubble
import com.github.sebsojeda.yapper.features.chat.presentation.components.ChatDate
import com.github.sebsojeda.yapper.features.chat.presentation.components.ChatInput
import com.github.sebsojeda.yapper.features.chat.presentation.components.ConversationBottomSheet
import com.github.sebsojeda.yapper.features.user.domain.model.User
import com.github.sebsojeda.yapper.ui.theme.Colors
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ChatDetailScreen(
    state: ChatDetailState,
    currentRoute: String?,
    navigateTo: (String) -> Unit,
    navigateBack: () -> Unit,
    createMessage: (String) -> Unit,
) {
    val auth = LocalAuthContext.current
    val localFocusManager = LocalFocusManager.current
    val columnState = rememberLazyListState()
    var showBottomSheet by remember { mutableStateOf(false) }

    fun parseDate(timestamp: String): String {
        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        val dateTime = ZonedDateTime.parse(timestamp, inputFormatter)
        val localDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())
        return localDateTime.format(outputFormatter)
    }

    LaunchedEffect(state.conversation?.messages?.size){
        columnState.animateScrollToItem(columnState.layoutInfo.totalItemsCount)
    }

    AppLayout(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    showBottomSheet = true
                }
            ) {
                if (state.conversation != null) {
                    Avatar(
                        imageUrl = state.conversation.media?.path,
                        displayName = state.conversation.name,
                        size = 32)
                    Text(
                        text = state.conversation.name,
                        modifier = Modifier.padding(top = 8.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        },
        currentRoute = currentRoute,
        navigateTo = navigateTo,
        navigationIcon = {
            IconButton(
                onClick = navigateBack,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left_outline),
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                if (state.isLoading) {
                    Loading(modifier = Modifier.align(Alignment.Center))
                } else if (state.error.isNotBlank()) {
                    Text(
                        text = state.error,
                        color = Colors.Red500,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (state.conversation == null || state.conversation.messages.isEmpty()) {
                    Text(
                        text = "No messages...yet",
                        color = Colors.Neutral400,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        state = columnState,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures { localFocusManager.clearFocus() }
                            }
                    ) {
                        items(state.conversation.messages.size) { messageIndex ->
                            val message = state.conversation.messages[messageIndex]
                            val messageDate = remember { parseDate(message.createdAt) }
                            val previousMessageDate = remember {
                                if (messageIndex > 0) {
                                    parseDate(state.conversation.messages[messageIndex - 1].createdAt)
                                } else {
                                    ""
                                }
                            }
                            if (messageDate != previousMessageDate) {
                                ChatDate(date = messageDate)
                            }
                            ChatBubble(
                                message = message.content,
                                align = if (message.user.id != auth.user.id) Alignment.Start else Alignment.End,
                                color = if (message.user.id != auth.user.id) Colors.Neutral400 else Colors.Indigo500,
                                timestamp = message.createdAt,
                                user = message.user,
                                showUser = message.user.id != auth.user.id && state.conversation.participants.size > 2
                            )
                        }
                    }
                }
            }
            ChatInput(
                focus = state.focusReply,
                onCreateMessage = createMessage,
                modifier = Modifier
                    .consumeWindowInsets(PaddingValues(innerPadding.calculateBottomPadding()))
                    .imePadding()
            )
        }
        if (showBottomSheet && state.conversation != null) {
            ConversationBottomSheet(
                conversation = state.conversation,
                onClose = { showBottomSheet = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatDetailScreenPreview() {
    ChatDetailScreen(
        state = ChatDetailState(
            conversationId = "1",
            conversation = Conversation(
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
                            name = "John",
                            username = "john",
                            createdAt = "2021-10-10T00:00:00Z",
                            avatar = null
                        ),
                        createdAt = "2021-10-10T00:00:00Z"
                    )
                ),
                createdAt = "2021-10-10T00:00:00Z",
                mediaId = null,
                media = null,
                messages = listOf(
                    Message(
                        id = "1",
                        content = "Hello",
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
                    Message(
                        id = "2",
                        content = "Hi",
                        conversationId = "1",
                        userId = "2",
                        user = User(
                            id = "2",
                            name = "John",
                            username = "john",
                            createdAt = "2021-10-10T00:00:00Z",
                            avatar = null
                        ),
                        createdAt = "2021-10-10T00:00:00Z"
                    ),
                    Message(
                        id = "3",
                        content = "How are you?",
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
                    Message(
                        id = "4",
                        content = "Good, you?",
                        conversationId = "1",
                        userId = "2",
                        user = User(
                            id = "2",
                            name = "John",
                            username = "john",
                            createdAt = "2021-10-10T00:00:00Z",
                            avatar = null
                        ),
                        createdAt = "2021-10-10T00:00:00Z"
                    ),
                    Message(
                        id = "5",
                        content = "I'm good",
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
                    Message(
                        id = "6",
                        content = "What are you doing?",
                        conversationId = "1",
                        userId = "2",
                        user = User(
                            id = "2",
                            name = "John",
                            username = "john",
                            createdAt = "2021-10-10T00:00:00Z",
                            avatar = null
                        ),
                        createdAt = "2021-10-11T00:00:00Z"
                    )
                )
            )
        ),
        currentRoute = ChatRoutes.ChatDetail.route,
        navigateTo = {},
        navigateBack = {},
        createMessage = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ChatDetailScreenPreviewLoading() {
    ChatDetailScreen(
        state = ChatDetailState(
            conversationId = "1",
            conversation = null,
            isLoading = true
        ),
        currentRoute = ChatRoutes.ChatDetail.route,
        navigateTo = {},
        navigateBack = {},
        createMessage = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ChatDetailScreenPreviewError() {
    ChatDetailScreen(
        state = ChatDetailState(
            conversationId = "1",
            conversation = null,
            error = "Error"
        ),
        currentRoute = ChatRoutes.ChatDetail.route,
        navigateTo = {},
        navigateBack = {},
        createMessage = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ChatDetailScreenPreviewNoMessages() {
    ChatDetailScreen(
        state = ChatDetailState(
            conversationId = "1",
            conversation = Conversation(
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
                            name = "John",
                            username = "john",
                            createdAt = "2021-10-10T00:00:00Z",
                            avatar = null
                        ),
                        createdAt = "2021-10-10T00:00:00Z"
                    )
                ),
                createdAt = "2021-10-10T00:00:00Z",
                mediaId = null,
                media = null,
                messages = emptyList()
            )
        ),
        currentRoute = ChatRoutes.ChatDetail.route,
        navigateTo = {},
        navigateBack = {},
        createMessage = {}
    )
}
