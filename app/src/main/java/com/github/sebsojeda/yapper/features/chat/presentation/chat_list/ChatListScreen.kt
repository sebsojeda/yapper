package com.github.sebsojeda.yapper.features.chat.presentation.chat_list

import Avatar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.components.YapperLayout
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.features.chat.presentation.ChatRoutes

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    YapperLayout (
        navController = navController,
        title = { Text(text = "Messages") },
        modifier = Modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ChatRoutes.ChatCreate.route) },
                modifier = Modifier,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pencil_outline),
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.value.conversations) { conversation ->
                    Row(
                        modifier = Modifier.fillMaxWidth().topBorder(1.dp, Color.LightGray).padding(16.dp).clickable {
                            navController.navigate(ChatRoutes.ChatDetail.route + "/${conversation.id}")
                        }
                    ) {
                        Avatar(
                            path = conversation.media?.path ?: conversation.messages?.last()?.user?.avatar?.path,
                            name = conversation.name ?: conversation.messages?.last()?.user?.name ?: "Unknown",
                            size = 48
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Column {
                            if (conversation.name != null) {
                                Text(text = conversation.name)
                            } else if (conversation.messages?.last()?.user?.name != null) {
                                Text(text = conversation.messages.last().user.name)
                            } else {
                                Text(text = "Unknown Conversation")
                            }
                            if (conversation.messages?.last() != null) {
                                Text(text = conversation.messages.last().content)
                            }
                        }
                    }
                }
            }
            if (state.value.error.isNotBlank()) {
                Text(text = state.value.error, modifier = Modifier.align(Alignment.Center))
            }
            if (state.value.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            if (!state.value.isLoading && state.value.conversations.isEmpty()) {
                Text(text = "No conversations...yet", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
