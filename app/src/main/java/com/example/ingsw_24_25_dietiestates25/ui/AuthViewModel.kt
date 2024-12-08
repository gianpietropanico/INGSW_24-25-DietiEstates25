package com.example.ingsw_24_25_dietiestates25.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthResult
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthState
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    // Espone un evento per notificare il successo del login
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    fun onEvent(event: AuthUiEvent) {
        Log.d("AuthViewModel", "Evento ricevuto: $event")
        when (event) {
            is AuthUiEvent.SignUpEmailChanged -> {
                _authState.value = _authState.value.copy(signUpEmail = event.value)
            }
            is AuthUiEvent.SignUpPasswordChanged -> {
                _authState.value = _authState.value.copy(signUpPassword = event.value)
            }
            is AuthUiEvent.SignUp -> {
                Log.d("AuthViewModel", "Inizio registrazione: ${_authState.value.signUpEmail}")
                signUpUser(_authState.value.signUpEmail, _authState.value.signUpPassword)
            }
            is AuthUiEvent.SignInEmailChanged -> {
                _authState.value = _authState.value.copy(signInEmail = event.value)
            }
            is AuthUiEvent.SignInPasswordChanged -> {
                _authState.value = _authState.value.copy(signInPassword = event.value)
            }
            is AuthUiEvent.SignIn -> {
                Log.d("AuthViewModel", "Inizio login: ${_authState.value.signInEmail}")
                signInUser(_authState.value.signInEmail, _authState.value.signInPassword)
            }
        }
    }


    private fun signUpUser(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)
            val result = authRepository.signUp(email, password)
            handleResult(result)
        }
    }

    private fun signInUser(email: String, password: String) {
        Log.d("AuthViewModel", "signInUser chiamato con email: $email e password: $password")
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)
            val result = authRepository.signIn(email, password)
            Log.d("AuthViewModel", "Risultato ricevuto da signIn: $result")
            handleResult(result)
        }
    }


    private suspend fun handleResult(result: AuthResult<Unit>) {
        when (result) {
            is AuthResult.Authorized -> {
                _authState.value = _authState.value.copy(isLoading = false)
                _toastMessage.emit("Sei stato loggato con successo!") // Notifica per il Toast
            }
            is AuthResult.Unauthorized -> {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = result.message
                )
            }
            is AuthResult.UnknownError -> {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = result.message
                )
            }
        }
    }
}