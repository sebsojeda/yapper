package com.github.sebsojeda.yapper.features.chat.presentation.chat_detail

import Avatar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.LocalAuthContext
import com.github.sebsojeda.yapper.core.components.AppLayout
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.features.chat.presentation.components.ChatBubble
import com.github.sebsojeda.yapper.features.chat.presentation.components.ConversationBottomSheet
import com.github.sebsojeda.yapper.ui.theme.Colors
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    state: ChatDetailState,
    currentRoute: String?,
    navigateTo: (String) -> Unit,
    navigateBack: () -> Unit,
    createMessage: (String) -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    val columnState = rememberLazyListState()
    val interactionSource = remember { MutableInteractionSource() }
    var content by remember { mutableStateOf("") }
    val auth = LocalAuthContext.current

    fun parseDate(timestamp: String): String {
        if (timestamp.isEmpty()) return ""
        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        val dateTime = ZonedDateTime.parse(timestamp, inputFormatter)
        val localDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())
        return localDateTime.format(outputFormatter)
    }

    LaunchedEffect(state.messages.size){
        columnState.animateScrollToItem(columnState.layoutInfo.totalItemsCount)
    }

    LaunchedEffect(state.focusReply) {
        if (state.focusReply) {
            focusRequester.requestFocus()
        }
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
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            LazyColumn(
                state = columnState,
                modifier = Modifier
                    .weight(1f)
                    .pointerInput(Unit) {
                        detectTapGestures { localFocusManager.clearFocus() }
                    }
            ) {
                items(state.messages) { message ->
                    val alignment: Alignment.Horizontal?
                    val color: Color?
                    if (message.user.id != auth.user.id) {
                        alignment = Alignment.Start
                        color = Colors.Neutral400
                    } else {
                        alignment = Alignment.End
                        color = Colors.Indigo500
                    }
                    val messageDate = remember { parseDate(message.createdAt) }
                    val previousMessageIndex = state.messages.indexOf(message) - 1
                    val previousMessageDate = remember {
                        if (previousMessageIndex >= 0) {
                            parseDate(state.messages[previousMessageIndex].createdAt)
                        } else {
                            ""
                        }
                    }
                    if (messageDate != previousMessageDate) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = messageDate,
                                modifier = Modifier,
                                color = Colors.Neutral400,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    ChatBubble(
                        message = message.content,
                        align = alignment,
                        color = color,
                        timestamp = message.createdAt
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .consumeWindowInsets(PaddingValues(innerPadding.calculateBottomPadding()))
                    .imePadding()
                    .topBorder(1.dp, Colors.Neutral200)
                    .padding(8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .clip(RoundedCornerShape(32.dp))
                        .background(Colors.Neutral200)
                ) {
                    BasicTextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged { hasFocus = it.isFocused },
                        decorationBox = @Composable { innerTextField ->
                            TextFieldDefaults.DecorationBox(
                                value = content,
                                innerTextField = innerTextField,
                                enabled = true,
                                singleLine = false,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = interactionSource,
                                placeholder = {
                                    Text(
                                        text = "Start a message",
                                        color = Colors.Neutral400
                                    )
                                },
                                contentPadding = PaddingValues(),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Colors.Transparent,
                                    unfocusedIndicatorColor = Colors.Transparent,
                                    focusedContainerColor = Colors.Transparent,
                                    focusedIndicatorColor = Colors.Transparent,
                                    cursorColor = Colors.Neutral950
                                )
                            )
                        }
                    )
                    IconButton(
                        onClick = {
                            createMessage(content)
                            content = ""
                        },
                        enabled = content.isNotBlank(),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Colors.Indigo500,
                            contentColor = Colors.White,
                            disabledContainerColor = Colors.Indigo300,
                            disabledContentColor = Colors.Indigo500
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.paper_airplane_mini),
                            contentDescription = null,
                        )
                    }
                }
            }
        }

        if (showBottomSheet && state.conversation != null) {
            ConversationBottomSheet(
                conversation = state.conversation,
                onClose = { showBottomSheet = false }
            )
        }
    }
}
