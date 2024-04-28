package com.github.sebsojeda.yapper.features.chat.presentation.chat_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.components.AppLayout
import com.github.sebsojeda.yapper.features.chat.presentation.ChatRoutes
import com.github.sebsojeda.yapper.features.chat.presentation.components.ChatListItem
import com.github.sebsojeda.yapper.ui.theme.Colors

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    AppLayout (
        navController = navController,
        title = { Text(text = "Messages") },
        modifier = Modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ChatRoutes.ChatCreate.route) },
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
                .padding(innerPadding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.value.conversations) { conversation ->
                    val firstParticipant = conversation.participants?.firstOrNull()
                    val lastMessage = conversation.messages?.lastOrNull()
                    val path = conversation.media?.path ?: firstParticipant?.user?.avatar?.path
                    val name = conversation.name ?: firstParticipant?.user?.name
                    val preview = lastMessage?.content
                    ChatListItem(img = path, name = name, preview = preview) {
                        navController.navigate("${ChatRoutes.ChatDetail.route}/${conversation.id}")
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
                Text(text = "No conversations...yet",
                    modifier = Modifier.align(Alignment.Center),
                    color = Colors.Neutral400
                )
            }
        }
    }
}
