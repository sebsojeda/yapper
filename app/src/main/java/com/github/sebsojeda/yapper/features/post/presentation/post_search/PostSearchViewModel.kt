package com.github.sebsojeda.yapper.features.post.presentation.post_search

import android.util.Log
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
class PostSearchViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(PostSearchState())
    val state: StateFlow<PostSearchState> = _state.asStateFlow()

    init {
        getPosts()
    }

    fun getPosts() {
        postUseCases.getPosts(Constants.POST_REFRESH_INTERVAL, "likes_count", true, 50).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(isLoading = true)
                is Resource.Success -> _state.value.copy(
                    posts = result.data ?: emptyList(),
                    isLoading = false
                )
                is Resource.Error -> _state.value.copy(
                    error = result.message ?: "An unexpected error occurred",
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun searchPosts(search: String) {
        if (search.isEmpty()) {
            getPosts()
            return
        }
        postUseCases.searchPosts(search).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(isLoading = true)
                is Resource.Success -> _state.value.copy(
                    posts = result.data ?: emptyList(),
                    isLoading = false
                )
                is Resource.Error -> _state.value.copy(
                    error = result.message ?: "An unexpected error occurred",
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onPostLikeClick(post: Post) {
        if (post.likedByUser) {
            postUseCases.unlikePost(post.id).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Do nothing
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            posts = _state.value.posts.map {
                                if (it.id == post.id) {
                                    it.copy(likedByUser = false, likes = it.likes - 1)
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
        } else {
            postUseCases.likePost(post.id).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Do nothing
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            posts = _state.value.posts.map {
                                if (it.id == post.id) {
                                    it.copy(likedByUser = true, likes = it.likes + 1)
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