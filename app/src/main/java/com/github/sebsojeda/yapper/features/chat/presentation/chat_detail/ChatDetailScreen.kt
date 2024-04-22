package com.github.sebsojeda.yapper.features.chat.presentation.chat_detail

import Avatar
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
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
                        fontWeight = FontWeight.Bold
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
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            LazyColumn(
                state = columnState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .pointerInput(Unit) {
                        detectTapGestures { localFocusManager.clearFocus() }
                    }
            ) {
                items(state.messages) { message ->
                    val horizontalArrangement = if (message.user.id == participant?.id) {
                        Arrangement.Start
                    } else {
                        Arrangement.End
                    }
                    val color = if (message.user.id == participant?.id) {
                        Color.LightGray
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    }
                    Row(
                        horizontalArrangement = horizontalArrangement,
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                    ) {
                        Text(
                            text = message.content,
                            color = Color.White,
                            modifier = Modifier
                                .background(color = color)
                                .padding(8.dp)
                                .clip(MaterialTheme.shapes.extraLarge)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .topBorder(1.dp, Color.LightGray)
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
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    ) {
                        Text("Send")
                    }
                }
                TextField(
                    value = viewModel.content,
                    onValueChange = { viewModel.onContentChange(it) },
                    placeholder = { Text("Start a message") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { hasFocus = it.isFocused },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.LightGray,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Transparent,
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
