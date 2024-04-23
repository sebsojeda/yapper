package com.github.sebsojeda.yapper.features.chat.presentation.chat_detail

import Avatar
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.components.YapperLayout
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.features.chat.presentation.ChatRoutes
import com.github.sebsojeda.yapper.features.chat.presentation.components.ChatBubble
import com.github.sebsojeda.yapper.ui.theme.Colors
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ChatDetailScreen(
    navController: NavController,
    viewModel: ChatDetailViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState().value
    val localFocusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    val participant = state.conversation?.participants?.get(0)?.user
    val columnState = rememberLazyListState()

    LaunchedEffect(state.messages.size){
        columnState.animateScrollToItem(columnState.layoutInfo.totalItemsCount)
    }

    YapperLayout(
        navController = navController,
        title = {
            participant?.let {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Avatar(
                        path = it.avatar?.path,
                        name = it.name,
                        size = 32)
                    Text(
                        text = it.name,
                        modifier = Modifier.padding(top = 8.dp),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = Colors.Neutral950
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigate(ChatRoutes.ChatList.route) {
                        popUpTo(ChatRoutes.ChatList.route) { inclusive = true }
                    }
                }
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
                    val align = if (message.user.id == participant?.id) {
                        Alignment.Start
                    } else {
                        Alignment.End
                    }
                    val color = if (message.user.id == participant?.id) {
                        Colors.Neutral400
                    } else {
                        // light blue
                        Colors.Indigo500
                    }
                    val messageDay = parseDay(message.createdAt)
                    val previousMessageIndex = state.messages.indexOf(message) - 1
                    val previousMessageDay = if (previousMessageIndex >= 0) {
                        parseDay(state.messages[previousMessageIndex].createdAt)
                    } else {
                        ""
                    }
                    if (messageDay != previousMessageDay) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .topBorder(1.dp, Colors.Neutral200)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = messageDay,
                                modifier = Modifier,
                                color = Colors.Neutral400,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    ChatBubble(
                        message = message.content,
                        align = align,
                        color = color,
                        timestamp = message.createdAt
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .topBorder(1.dp, Colors.Neutral200)
                    .padding(8.dp),
            ) {
                if (hasFocus) {
                    Button(
                        onClick = {
                            viewModel.onSendMessageClick()
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(bottom = 8.dp),
                        enabled = viewModel.content.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Colors.Indigo500)
                    ) {
                        Text("Send")
                    }
                }
                TextField(
                    value = viewModel.content,
                    onValueChange = { viewModel.onContentChange(it) },
                    placeholder = { Text("Start a message", color = Colors.Neutral400) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { hasFocus = it.isFocused },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Colors.Neutral200,
                        unfocusedIndicatorColor = Colors.Transparent,
                        focusedContainerColor = Colors.Neutral200,
                        focusedIndicatorColor = Colors.Transparent,
                    ),
                    shape = MaterialTheme.shapes.extraLarge
                )
            }
        }
    }

    LaunchedEffect(state.focusReply) {
        if (state.focusReply) {
            focusRequester.requestFocus()
        }
    }
}

fun parseDay(timestamp: String): String {
    if (timestamp.isEmpty()) return ""
    val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")

    val dateTime = ZonedDateTime.parse(timestamp, inputFormatter)
    val localDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())

    return localDateTime.format(outputFormatter)
}
