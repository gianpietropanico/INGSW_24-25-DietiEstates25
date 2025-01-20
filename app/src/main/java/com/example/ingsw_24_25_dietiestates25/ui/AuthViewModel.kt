package com.example.ingsw_24_25_dietiestates25.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthResponse
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthResult
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthState
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthUiEvent
import com.example.ingsw_24_25_dietiestates25.data.model.User
import com.example.ingsw_24_25_dietiestates25.data.model.UserPayload
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    // Stato per verificare se l'utente è loggato
    private val _isLoggedIn = MutableStateFlow(false) // Stato iniziale non loggato
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // Espone un evento per notificare il successo del login
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    private val _authSuccess = MutableSharedFlow<Unit>() // Evento per notificare il successo
    val authSuccess: SharedFlow<Unit> = _authSuccess

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
                signUpUser(_authState.value.signUpEmail, _authState.value.signUpPassword)
            }

            is AuthUiEvent.SignInEmailChanged -> {
                _authState.value = _authState.value.copy(signInEmail = event.value)
            }

            is AuthUiEvent.SignInPasswordChanged -> {
                _authState.value = _authState.value.copy(signInPassword = event.value)
            }

            is AuthUiEvent.SignIn -> {
                signInUser(_authState.value.signInEmail, _authState.value.signInPassword)
            }

            is AuthUiEvent.SignUpConfirmPasswordChanged -> {
                _authState.value = _authState.value.copy(signUpConfirmPassword = event.value)
            }

        }
    }

    private fun signUpUser(email: String, password: String) {
        if (_authState.value.signUpPassword == _authState.value.signUpConfirmPassword) {
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

    private fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)
            val result = authRepository.signIn(email, password)
            handleResult(result)
        }
    }

    private suspend fun handleResult(result: AuthResult<Unit>) {
        when (result) {
            is AuthResult.Authorized -> {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    isAuthenticated = true
                )
                _toastMessage.emit("Sei stato loggato con successo!")
                _authSuccess.emit(Unit)
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
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                _isLoggedIn.value = false
                _toastMessage.emit("Sei stato disconnesso.")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Errore durante il logout", e)
                _toastMessage.emit("Errore durante il logout.")
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

}


//class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
//
//    private val _authState = MutableStateFlow(AuthState())
//    val authState: StateFlow<AuthState> = _authState
//
//    // Stato per verificare se l'utente è loggato
//    private val _isLoggedIn = MutableStateFlow(false) // Stato iniziale non loggato
//    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn
//
//    // Espone un evento per notificare il successo del login
//    private val _toastMessage = MutableSharedFlow<String>()
//    val toastMessage: SharedFlow<String> = _toastMessage
//
//    private val _authSuccess = MutableSharedFlow<Unit>() // Evento per notificare il successo
//    val authSuccess: SharedFlow<Unit> = _authSuccess
//
//    init {
//        // Inizializza il GitHubCallbackManager per gestire i codici di autenticazione
//        GitHubCallbackManager.registerCallback { code ->
//            if (code.isNotEmpty()) {
//                Log.d("GitHubCallbackManager", "Codice ricevuto dal redirect URI: $code")
//                authenticateWithJwt(code)
//            } else {
//                Log.e("GitHubCallbackManager", "Errore: il codice non è presente nel redirect URI.")
//            }
//        }
//    }
//
//    fun onEvent(event: AuthUiEvent) {
//        Log.d("AuthViewModel", "Evento ricevuto: $event")
//        when (event) {
//            is AuthUiEvent.SignUpEmailChanged -> {
//                _authState.value = _authState.value.copy(signUpEmail = event.value)
//            }
//            is AuthUiEvent.SignUpPasswordChanged -> {
//                _authState.value = _authState.value.copy(signUpPassword = event.value)
//            }
//            is AuthUiEvent.SignUp -> {
//                signUpUser(_authState.value.signUpEmail, _authState.value.signUpPassword)
//            }
//            is AuthUiEvent.SignInEmailChanged -> {
//                _authState.value = _authState.value.copy(signInEmail = event.value)
//            }
//            is AuthUiEvent.SignInPasswordChanged -> {
//                _authState.value = _authState.value.copy(signInPassword = event.value)
//            }
//            is AuthUiEvent.SignIn -> {
//                signInUser(_authState.value.signInEmail, _authState.value.signInPassword)
//            }
//            is AuthUiEvent.SignUpConfirmPasswordChanged -> {
//                _authState.value = _authState.value.copy(signUpConfirmPassword = event.value)
//            }
//            is AuthUiEvent.GitHubLogin -> {
//                authenticateWithJwt(event.code)
//            }
//        }
//    }
//
//    private fun signUpUser(email: String, password: String) {
//        if (_authState.value.signUpPassword == _authState.value.signUpConfirmPassword) {
//            viewModelScope.launch {
//                _authState.value = _authState.value.copy(isLoading = true)
//                val result = authRepository.signUp(email, password)
//                handleResult(result)
//            }
//        } else {
//            _authState.value = _authState.value.copy(
//                isLoading = false,
//                errorMessage = "Le password non combaciano"
//            )
//        }
//    }
//
//    private fun signInUser(email: String, password: String) {
//        viewModelScope.launch {
//            _authState.value = _authState.value.copy(isLoading = true)
//            val result = authRepository.signIn(email, password)
//            handleResult(result)
//        }
//    }
//
//    private suspend fun handleResult(result: AuthResult<Unit>) {
//        when (result) {
//            is AuthResult.Authorized -> {
//                _authState.value = _authState.value.copy(
//                    isLoading = false,
//                    isAuthenticated = true
//                )
//                _toastMessage.emit("Sei stato loggato con successo!")
//                _authSuccess.emit(Unit)
//            }
//            is AuthResult.Unauthorized -> {
//                _authState.value = _authState.value.copy(
//                    isLoading = false,
//                    isAuthenticated = false,
//                    errorMessage = result.message
//                )
//            }
//            is AuthResult.UnknownError -> {
//                _authState.value = _authState.value.copy(
//                    isLoading = false,
//                    isAuthenticated = false,
//                    errorMessage = result.message
//                )
//            }
//        }
//    }
//
//    fun logout() {
//        viewModelScope.launch {
//            try {
//                authRepository.logout()
//                _isLoggedIn.value = false
//                _toastMessage.emit("Sei stato disconnesso.")
//            } catch (e: Exception) {
//                Log.e("AuthViewModel", "Errore durante il logout", e)
//                _toastMessage.emit("Errore durante il logout.")
//            }
//        }
//    }
//
//    fun authenticateWithJwt(code: String) {
//        viewModelScope.launch {
//            _authState.value = _authState.value.copy(isLoading = true)
//            val jwtToken = authRepository.fetchJwtFromServer(code) // Ottieni direttamente il JWT
//            if (jwtToken != null) {
//                saveJwtToken(jwtToken)
//                _authState.value = _authState.value.copy(isAuthenticated = true)
//                _toastMessage.emit("Autenticazione completata con successo!")
//            } else {
//                _authState.value = _authState.value.copy(
//                    errorMessage = "Autenticazione fallita",
//                    isAuthenticated = false
//                )
//                _toastMessage.emit("Errore durante l'autenticazione.")
//            }
//            _authState.value = _authState.value.copy(isLoading = false)
//        }
//    }
//
//
//    private fun saveJwtToken(jwtToken: String) {
//        _authState.value = _authState.value.copy(jwtToken = jwtToken)
//        _isLoggedIn.value = true
//        Log.d("AuthViewModel", "Token JWT salvato: $jwtToken")
//    }
//}