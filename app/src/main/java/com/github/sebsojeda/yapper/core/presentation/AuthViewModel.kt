package com.github.sebsojeda.yapper.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sebsojeda.yapper.features.user.data.dto.toUser
import com.github.sebsojeda.yapper.features.user.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: Auth,
    private val userRepository: UserRepository,
): ViewModel() {
    private val _state: MutableStateFlow<AuthState> = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    init {
        auth.sessionStatus.onEach { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    val user = userRepository.getUser(auth.currentUserOrNull()!!.id).toUser()
                    _state.value = _state.value.copy(
                        currentUser = user,
                        isAuthenticated = true
                    )
                }
                else -> {
                    _state.value = _state.value.copy(
                        currentUser = null,
                        isAuthenticated = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                auth.signOut()
                _state.value = _state.value.copy(
                    currentUser = null,
                    isAuthenticated = false
                )
            } catch (_: Exception) { }
        }
    }
}
