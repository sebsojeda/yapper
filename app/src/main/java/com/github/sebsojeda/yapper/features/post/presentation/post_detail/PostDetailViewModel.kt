package com.github.sebsojeda.yapper.features.post.presentation.post_detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.core.domain.model.MediaUpload
import com.github.sebsojeda.yapper.features.post.domain.model.Comment
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

    fun getPost(postId: String) {
        postUseCases.getPost(postId).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(isPostLoading = true)
                is Resource.Success -> {
                    Log.d("PostDetailViewModel", "Post: ${result.data}")
                    _state.value.copy(
                        post = result.data,
                        isPostLoading = false
                    )
                }
                is Resource.Error -> _state.value.copy(
                    postError = result.message ?: "An unexpected error occurred",
                    isPostLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun getComments(postId: String) {
        postUseCases.getComments(postId).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(areCommentsLoading = true)
                is Resource.Success -> _state.value.copy(
                    comments = result.data ?: emptyList(),
                    areCommentsLoading = false
                )
                is Resource.Error -> _state.value.copy(
                    commentsError = result.message ?: "An unexpected error occurred",
                    areCommentsLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onPostCommentClick(content: String, media: List<MediaUpload>) {
        postUseCases.createComment(content, _state.value.postId, media).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    // Do nothing
                }
                is Resource.Success -> {
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

    fun onToggleLike(post: Post) {
        postUseCases.toggleLike(post.id, post.likedByUser).onEach { result ->
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
                            likes = if (post.likedByUser) post.likes - 1 else post.likes + 1,
                            likedByUser = !post.likedByUser
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onToggleCommentLike(comment: Comment) {
        postUseCases.toggleLike(comment.id, comment.likedByUser).onEach { result ->
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
                                    likes = if (it.likedByUser) it.likes - 1 else it.likes + 1,
                                    likedByUser = !it.likedByUser
                                )
                            } else {
                                it
                            }
                        }
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}