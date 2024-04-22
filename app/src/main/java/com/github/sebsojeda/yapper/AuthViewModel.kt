package com.github.sebsojeda.yapper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sebsojeda.yapper.features.user.data.dto.toUser
import com.github.sebsojeda.yapper.features.user.domain.model.User
import com.github.sebsojeda.yapper.features.user.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: Auth,
    private val userRepository: UserRepository
): ViewModel() {
    private val _isAuthenticated = MutableStateFlow(auth.currentUserOrNull() != null)
    private val _currentUser = MutableStateFlow<User?>(null)
    val isAuthenticated = _isAuthenticated.asStateFlow()
    val currentUser = _currentUser.asStateFlow()

    init {
        auth.sessionStatus.onEach { status ->
            _isAuthenticated.value = when (status) {
                is SessionStatus.Authenticated -> {
                    true
                }
                else -> {
                    false
                }
            }
        }.launchIn(viewModelScope)
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                auth.signOut()
            } catch (_: Exception) { }
        }
    }

    fun currentUser() {
        viewModelScope.launch {
            try {
                _currentUser.value = userRepository.getUser(auth.currentUserOrNull()?.id ?: "").toUser()
            } catch (_: Exception) { }
        }
    }
}
