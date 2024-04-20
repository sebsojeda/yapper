package com.github.sebsojeda.yapper.features.authentication.presentation.sign_up_confirmation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sebsojeda.yapper.core.Resource
import com.github.sebsojeda.yapper.features.authentication.domain.usecase.AuthenticationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
open class SignUpConfirmationViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases,
    stateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(SignUpConfirmationState())
    val state: StateFlow<SignUpConfirmationState> = _state.asStateFlow()

    init {
        stateHandle.get<String>("email")?.let { email ->
            _state.value = _state.value.copy(email = email)
        }
    }

    fun resendEmail() {
        authenticationUseCases.signUpConfirmation(_state.value.email).onEach { result ->
                _state.value = when(result) {
                    is Resource.Loading -> _state.value.copy(isLoading = true)
                    is Resource.Success -> _state.value.copy(isLoading = false)
                    is Resource.Error -> _state.value.copy(
                        error = result.message ?: "An unexpected error occurred",
                        isLoading = false
                    )
                }
        }.launchIn(viewModelScope)
    }
}