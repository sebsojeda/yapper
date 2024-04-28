package com.github.sebsojeda.yapper.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sebsojeda.yapper.features.user.data.dto.toUser
import com.github.sebsojeda.yapper.features.user.domain.model.User
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
class MainViewModel @Inject constructor(
    private val auth: Auth,
    private val userRepository: UserRepository,
): ViewModel() {
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        auth.sessionStatus.onEach { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    val user = userRepository.getUser(auth.currentUserOrNull()!!.id).toUser()
                    _currentUser.value = user
                    _isAuthenticated.value = true
                }
                else -> {
                    _isAuthenticated.value = false
                }
            }
        }.launchIn(viewModelScope)
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                auth.signOut()
                _isAuthenticated.value = false
            } catch (_: Exception) { }
        }
    }
}
