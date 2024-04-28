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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.components.AppLayout
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.features.chat.presentation.ChatRoutes
import com.github.sebsojeda.yapper.features.chat.presentation.components.ChatBubble
import com.github.sebsojeda.yapper.ui.theme.Colors
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    navController: NavController,
    viewModel: ChatDetailViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState().value
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    var hasFocus by remember { mutableStateOf(false) }
    val columnState = rememberLazyListState()
    var newConversationName by remember { mutableStateOf(state.conversation?.name) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(state.messages.size){
        columnState.animateScrollToItem(columnState.layoutInfo.totalItemsCount)
    }

    AppLayout(
        navController = navController,
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
                    val align: Alignment.Horizontal?
                    val color: Color?
                    if (state.conversation?.participants?.any { it.userId == message.user.id } == true) {
                        align = Alignment.Start
                        color = Colors.Neutral400
                    } else {
                        align = Alignment.End
                        color = Colors.Indigo500
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
//                    RoundedInputField(
//                        value = viewModel.content,
//                        onValueChange = { viewModel.onContentChange(it) },
//                        placeholder = "Start a message",
//                        modifier =  Modifier
//                            .fillMaxWidth()
//                            .focusRequester(focusRequester)
//                            .onFocusChanged { hasFocus = it.isFocused },
//                        suffix = {
//                            IconButton(
//                                onClick = {
//                                    viewModel.onSendMessageClick()
//                                },
//                                modifier = Modifier,
//                                enabled = viewModel.content.isNotBlank(),
//                                colors = IconButtonDefaults.iconButtonColors(
//                                    containerColor = Colors.Indigo500,
//                                    contentColor = Colors.White,
//                                    disabledContainerColor = Colors.Indigo300,
//                                    disabledContentColor = Colors.Indigo500
//                                )
//                            ) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.paper_airplane_mini),
//                                    contentDescription = null,
//                                )
//                            }
//                        }
//                    )
                    BasicTextField(
                        value = viewModel.content,
                        onValueChange = { viewModel.onContentChange(it) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged { hasFocus = it.isFocused },
                        decorationBox = @Composable { innerTextField ->
                            TextFieldDefaults.DecorationBox(
                                value = viewModel.content,
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
                                )
                            )
                        }
                    )
                    IconButton(
                        onClick = {
                            viewModel.onSendMessageClick()
                        },
                        modifier = Modifier,
                        enabled = viewModel.content.isNotBlank(),
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
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
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
                        imageUrl = state.conversation.media?.path,
                        displayName = state.conversation.name,
                        size = 64)
                    Box {
                        TextField(
                            value = newConversationName ?: state.conversation.name,
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
                        newConversationName = state.conversation.name
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
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
