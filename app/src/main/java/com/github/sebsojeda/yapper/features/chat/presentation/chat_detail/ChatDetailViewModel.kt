package com.github.sebsojeda.yapper.features.chat.presentation.chat_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.chat.domain.usecase.ChatUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<ChatDetailState> = MutableStateFlow(ChatDetailState())
    val state: StateFlow<ChatDetailState> = _state

    init {
        savedStateHandle.get<String>(Constants.PARAM_CONVERSATION_ID)?.let { conversationId ->
            getMessages(conversationId)
            listenForMessages(conversationId)
        }
    }

    private fun getMessages(conversationId: String) {
        chatUseCases.getConversation(conversationId).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(isLoading = true)
                is Resource.Success -> {
                    _state.value.copy(
                        conversation = result.data,
                        messages = result.data?.messages ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> _state.value.copy(
                    error = result.message ?: "An unexpected error occurred",
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun listenForMessages(conversationId: String) {
        chatUseCases.listenToMessages(conversationId).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(isLoading = true)
                is Resource.Success -> {
                    _state.value.copy(
                        messages = _state.value.messages + result.data!!,
                        isLoading = false
                    )
                }
                is Resource.Error -> _state.value.copy(
                    error = result.message ?: "An unexpected error occurred",
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun createMessage(content: String) {
        chatUseCases.createMessage(_state.value.conversationId, content).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    // Do nothing
                }
                is Resource.Success -> {

                }
                is Resource.Error -> {
                    // Do nothing
                }
            }
        }.launchIn(viewModelScope)
    }
}