package com.github.sebsojeda.yapper.features.post.presentation.post_detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.post.domain.model.Post
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
    var content by mutableStateOf("")
        private set

    init {
        savedStateHandle.get<String>(Constants.PARAM_POST_ID)?.let { postId ->
            _state.value = _state.value.copy(postId = postId)
            getPost(postId)
            getComments(postId)
        }
        savedStateHandle.get<Boolean>(Constants.PARAM_FOCUS_REPLY)?.let {
            _state.value = _state.value.copy(focusReply = it)
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

    fun onPostCommentClick(media: List<ByteArray> = emptyList()) {
        postUseCases.createPost(content, _state.value.postId, media).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    // Do nothing
                }
                is Resource.Success -> {
                    this.content = ""
                    _state.value = _state.value.copy(
                        post = _state.value.post?.copy(comments = _state.value.post?.comments?.plus(1) ?: 0),
                        comments = _state.value.comments + result.data!!
                    )
                }
                is Resource.Error -> {
                    // Do nothing
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onContentChange(content: String) {
        this.content = content
    }

    fun onPostLikeClick(post: Post) {
        if (post.likedByUser) {
            postUseCases.unlikePost(post.id).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.e("PostDetailViewModel", "Error: ${result.message}")
                    }
                    is Resource.Loading -> {
                        // Do nothing
                    }
                    is Resource.Success -> {
                        // update post or comment
                        _state.value = _state.value.copy(
                            post = _state.value.post?.copy(
                                likes = _state.value.post?.likes?.minus(1) ?: 0,
                                likedByUser = false
                            )
                        )
                    }
                }
            }.launchIn(viewModelScope)
        } else {
            postUseCases.likePost(post.id).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Do nothing
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            post = _state.value.post?.copy(
                                likes = _state.value.post?.likes?.plus(1) ?: 0,
                                likedByUser = true
                            )
                        )
                    }
                    is Resource.Error -> {
                        Log.e("PostDetailViewModel", "Error: ${result.message}")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onCommentLikeClick(comment: Post) {
        if (comment.likedByUser) {
            postUseCases.unlikePost(comment.id).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.e("PostDetailViewModel", "Error: ${result.message}")
                    }
                    is Resource.Loading -> {
                        // Do nothing
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            comments = _state.value.comments.map {
                                if (it.id == comment.id) {
                                    it.copy(
                                        likes = it.likes - 1,
                                        likedByUser = false
                                    )
                                } else {
                                    it
                                }
                            }
                        )
                    }
                }
            }.launchIn(viewModelScope)
        } else {
            postUseCases.likePost(comment.id).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Do nothing
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            comments = _state.value.comments.map {
                                if (it.id == comment.id) {
                                    it.copy(
                                        likes = it.likes + 1,
                                        likedByUser = true
                                    )
                                } else {
                                    it
                                }
                            }
                        )
                    }
                    is Resource.Error -> {
                        Log.e("PostDetailViewModel", "Error: ${result.message}")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}