package com.github.sebsojeda.yapper.features.authentication.presentation.sign_up

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
class SignUpViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases,
): ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    fun signUp(name: String, email: String, password: String) {
        authenticationUseCases.signUp(name, email, password).onEach { result ->
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