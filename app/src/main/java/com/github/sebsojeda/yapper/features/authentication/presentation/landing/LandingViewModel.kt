package com.github.sebsojeda.yapper.features.authentication.presentation.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
open class LandingViewModel @Inject constructor(
    private val sessionStatus: StateFlow<@JvmSuppressWildcards SessionStatus>,
): ViewModel() {
    private val _state = MutableStateFlow(LandingState())
    val state: StateFlow<LandingState> = _state.asStateFlow()

    init {
        getSessionStatus()
    }

    private fun getSessionStatus() {
        sessionStatus.onEach { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    _state.value = _state.value.copy(isAuthenticated = true)
                }
                else -> {
                    _state.value = _state.value.copy(isAuthenticated = false)
                }
            }
        }.launchIn(viewModelScope)
    }
}