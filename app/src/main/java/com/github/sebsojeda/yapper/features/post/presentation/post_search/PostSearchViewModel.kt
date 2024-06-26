package com.github.sebsojeda.yapper.features.post.presentation.post_search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.core.domain.model.MediaUpload
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

    fun search(search: String) {
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

    fun toggleLike(post: Post) {
        postUseCases.toggleLike(post.id, post.likedByUser).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    // Do nothing
                }
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        posts = _state.value.posts.map {
                            if (it.id == post.id) {
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
                is Resource.Error -> {
                    Log.e("PostDetailViewModel", "Error: ${result.message}")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun createPost(content: String, media: List<MediaUpload>) {
        postUseCases.createPost(content, media).onEach { result ->
            Log.d("PostListViewModel", "createPost: ${result.message}")
            when (result) {
                is Resource.Loading -> {}
                is Resource.Success -> {}
                is Resource.Error -> {}
            }
        }.launchIn(viewModelScope)
    }
}