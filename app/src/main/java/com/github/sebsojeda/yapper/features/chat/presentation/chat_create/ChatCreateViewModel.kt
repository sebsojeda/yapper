package com.github.sebsojeda.yapper.features.chat.presentation.chat_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.chat.domain.usecase.ChatUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatCreateViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
): ViewModel() {
    private val _state = MutableStateFlow(ChatCreateState())
    val state: StateFlow<ChatCreateState> = _state.asStateFlow()

    fun createChat(participants: List<String>, content: String) {
        chatUseCases.createConversation(participants, content).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            conversationId = result.data?.id,
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _state.value = ChatCreateState(
                            error = result.message ?: "An unexpected error occurred",
                            isLoading = false
                        )
                    }
                }
        }.launchIn(viewModelScope)
    }
}