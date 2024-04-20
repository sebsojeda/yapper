package com.github.sebsojeda.yapper.features.post.presentation.post_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.post.domain.usecase.PostUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(PostDetailState())
    val state: StateFlow<PostDetailState> = _state.asStateFlow()

    init {
        savedStateHandle.get<String>(Constants.PARAM_POST_ID)?.let { postId ->
            getPost(postId)
            getComments(postId)
        }
    }

    private fun getPost(postId: String) {
        postUseCases.getPost(postId).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(isPostLoading = true)
                is Resource.Success -> _state.value.copy(
                    post = result.data,
                    isPostLoading = false
                )
                is Resource.Error -> _state.value.copy(
                    postError = result.message ?: "An unexpected error occurred",
                    isPostLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun getComments(postId: String) {
        postUseCases.getPostComments(postId).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(isCommentsLoading = true)
                is Resource.Success -> _state.value.copy(
                    comments = result.data ?: emptyList(),
                    isCommentsLoading = false
                )
                is Resource.Error -> _state.value.copy(
                    commentsError = result.message ?: "An unexpected error occurred",
                    isCommentsLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onPostLikeClick(postId: String) {

    }
}