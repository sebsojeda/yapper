package com.github.sebsojeda.yapper.features.chat.presentation.chat_list

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
class ChatListViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(ChatListState())
    val state: StateFlow<ChatListState> = _state.asStateFlow()

    init {
        getConversations()
    }

    private fun getConversations() {
        chatUseCases.getConversations().onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(isLoading = true)
                is Resource.Success -> _state.value.copy(
                    conversations = result.data ?: emptyList(),
                    isLoading = false
                )
                is Resource.Error -> _state.value.copy(
                    error = result.message ?: "An unexpected error occurred",
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun createChat(participants: List<String>, content: String) {
        chatUseCases.createConversation(participants, content).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> {
                    _state.value.copy(isChatLoading = true)
                }
                is Resource.Success -> {
                    _state.value.copy(
                        chat = result.data,
                        isChatLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value.copy(
                        error = result.message ?: "An unexpected error occurred",
                        isChatLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}