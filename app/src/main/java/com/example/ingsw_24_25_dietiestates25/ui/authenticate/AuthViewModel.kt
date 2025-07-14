package com.example.ingsw_24_25_dietiestates25.ui.authenticate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.dieti_estate.ui.authenticate.AuthState
import com.example.ingsw_24_25_dietiestates25.data.repository.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.authenticate.AuthResult
import com.example.ingsw_24_25_dietiestates25.model.authenticate.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor (
    private val authRepository: AuthRepository,
    val userSessionManager: UserSessionManager
) : ViewModel() {

    val user = userSessionManager.currentUser

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    fun signUpUser(email: String, password: String) {
        if (_authState.value.password == _authState.value.confirmPassword) {
            viewModelScope.launch {
                _authState.value = _authState.value.copy(isLoading = true)
                val result = authRepository.signUp(email, password)
                handleResult(result)
            }
        } else {
            _authState.value = _authState.value.copy(
                isLoading = false,
                errorMessage = "Le password non combaciano"
            )
        }
    }

    fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)
            val result = authRepository.signIn(email, password)
            handleResult(result)
        }
    }

    fun sendResetPasswordEmail(email: String, newPassword :String, oldPassword: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = authRepository.resetPassword(email, oldPassword, newPassword)
                handleResult(result)
            } catch (e: Exception) {
                _authState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to send reset link: ${e.message}"
                    )
                }
            }
        }
    }

    private fun handleResult(result: AuthResult<Unit>) {
        when (result) {
            is AuthResult.Authorized -> {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    isAuthenticated = true
                )
            }

            is AuthResult.Unauthorized -> {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    isAuthenticated = false,
                    errorMessage = result.message
                )
            }

            is AuthResult.UnknownError -> {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    isAuthenticated = false,
                    errorMessage = result.message
                )
            }

            is AuthResult.Success -> {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    isAuthenticated = false,
                    errorMessage = result.message
                )
            }
        }
    }


    suspend fun notifyServer(code: String?, state: String?): User? {
        return try {
            // Aggiorna lo stato per indicare che è in corso un'operazione
            _authState.value = _authState.value.copy(isLoading = true)

            // Chiama il repository per scambiare il codice
            val notify = authRepository.notifyServer(code, state)

            // Controlla se l'utente è stato recuperato correttamente
            if (notify is AuthResult.Authorized && notify.data != null) {
                val user = notify.data
                Log.d("AuthViewModel", "Utente ricevuto: $user")

                // Aggiorna lo stato dell'app
                _authState.value = _authState.value.copy(isLoading = false)
                user // Restituisce l'oggetto User
            } else {
                // Se non è stato possibile recuperare l'utente
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = "Errore sconosciuto durante il recupero dell'utente."
                )
                Log.e("AuthViewModel", "Errore sconosciuto durante il recupero dell'utente.")
                null
            }
        } catch (e: Exception) {
            // Gestione degli errori
            _authState.value = _authState.value.copy(
                isLoading = false,
                errorMessage = e.message ?: "Errore durante il recupero dell'utente."
            )
            Log.e("AuthViewModel", "Errore durante il recupero dell'utente", e)
            null
        }
    }


    suspend fun fetchState(): String? {

       return try {
           // Aggiorna lo stato per indicare che è in corso un'operazione
           _authState.value = _authState.value.copy(isLoading = true)

           // Chiama il repository per ottenere lo stato dal server
           val stateResult = authRepository.fetchState()

           // Controlla se lo stato è stato recuperato correttamente
           if (stateResult is AuthResult.Authorized && !stateResult.data.isNullOrEmpty()) {
               val state = stateResult.data
               Log.d("AuthViewModel", "Stato recuperato: $state")

               // Aggiorna lo stato dell'app
               _authState.value = _authState.value.copy(isLoading = false)
               state // Restituisce lo stato recuperato
           } else {
               // Se non è stato possibile recuperare lo stato
               _authState.value = _authState.value.copy(
                   isLoading = false,
                   errorMessage = "Errore sconosciuto durante il recupero dello stato."
               )
               Log.e("AuthViewModel", "Errore sconosciuto durante il recupero dello stato.")
               null
           }
       } catch (e: Exception) {
           // Gestione degli errori
           _authState.value = _authState.value.copy(
               isLoading = false,
               errorMessage = e.message ?: "Errore durante il recupero dello stato."
           )
           Log.e("AuthViewModel", "Errore durante il fetch dello stato", e)
           null
       }

    }

    fun clearErrorMessage() {
        _authState.update { it.copy(errorMessage = null) }
    }

    fun logout(){
        userSessionManager.clear()
    }
}
