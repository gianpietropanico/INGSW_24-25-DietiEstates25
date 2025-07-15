package com.example.ingsw_24_25_dietiestates25.ui.authenticate

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieti_estate.ui.authenticate.AuthState
import com.example.ingsw_24_25_dietiestates25.data.repository.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.authenticate.AuthResult
import com.example.ingsw_24_25_dietiestates25.model.authenticate.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun authWithThirdParty(email : String?, username :String?) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)

            val result = if ( email == null || username == null ) AuthResult.Unauthorized("Problema con OUATH")
                         else authRepository.authWithThirdParty(email, username)

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

    fun clearErrorMessage() {
        _authState.update { it.copy(errorMessage = null) }
    }

    fun unAuthorized( error_msg : String){
        _authState.value = _authState.value.copy(
            isLoading = false,
            isAuthenticated = false,
            errorMessage = error_msg,
        )
    }

    fun logout(){
        _authState.value = _authState.value.copy(
            isLoading = false,
            isAuthenticated = false
        )

        userSessionManager.clear()
    }

    suspend fun notifyServer(code: String?, state: String?): User? {
        return try {
            // Aggiorna lo stato per indicare che è in corso un'operazione
            _authState.value = _authState.value.copy(isLoading = true)

            // Chiama il repository per scambiare il codice
            val notify = authRepository.exchangeGitHubCode(code, state)

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

    private fun startGitHubLogin(state: String, context: Context) {
        val clientId = "Ov23liKKZ3TLrVsufnT8"
        val redirectUri = "dietiestates25://callback/github"
        val authUrl = "https://github.com/login/oauth/authorize?" +
                "client_id=$clientId" +
                "&scope=read:user,user:email" +
                "&state=$state"

        Log.d("openGitHubOAuth", "Generated Auth URL: $authUrl")

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(intent)
            Log.d("openGitHubOAuth", "OAuth intent started successfully.")
        } catch (e: Exception) {
            Log.e("openGitHubOAuth", "Error launching intent: ${e.message}", e)
        }

    }

    fun handleCallback(code: String, state: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // 1) Scambia code e state per ottenere l’utente
                authRepository.exchangeGitHubCode(code, state)

                // 3) Aggiorna lo stato con l’utente e flag di successo
                _authState.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = true,
                    )
                }
            } catch (e: Exception) {
                _authState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Errore durante l’autenticazione"
                    )
                }
            }
        }
    }

    fun openGitHubOAuth(context: Context, state: String) {
        // 1) Ricostruisci l’URL in modo corretto
        val clientId = "Ov23liKKZ3TLrVsufnT8"
        val uri = Uri.Builder()
            .scheme("https")
            .authority("github.com")
            .appendPath("login")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("scope", "read:user user:email")
            .appendQueryParameter("state", state)
            .build()

        Log.d("openGitHubOAuth", "Built OAuth URL: $uri")

        // 2) Prepara Custom Tabs e forza Chrome
        val customTabsIntent = CustomTabsIntent.Builder().build().apply {
            intent.setPackage("com.android.chrome")
        }

        // 3) Prova a lanciare Custom Tab, se fallisce usa Intent.ACTION_VIEW
        try {
            customTabsIntent.launchUrl(context, uri)
        } catch (e: Exception) {
            Log.w("openGitHubOAuth", "CustomTab failed, fallback to ACTION_VIEW", e)
            val viewIntent = Intent(Intent.ACTION_VIEW, uri)
            if (viewIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(viewIntent)
            } else {
                Toast.makeText(context, "Nessun browser disponibile", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun launchGithubOauth(context: Context) {
        viewModelScope.launch {
            // 1) metti loader
            _authState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // 2) prendi lo state dal server
                val result = authRepository.fetchState()
                if (result is AuthResult.Success && !result.data.isNullOrBlank()) {
                    val state = result.data

                    // 3) apri GitHub nel browser con client_id, scope e state
                    openGitHubOAuth(context, state)

                    // non fermare subito il loader: aspetta che torni il deep link
                } else {
                    // errore nel fetch dello state
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Impossibile generare lo stato CSRF."
                        )
                    }
                }

            } catch (e: Throwable) {
                // gestione eccezioni generiche
                _authState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Errore durante l'avvio di GitHub OAuth."
                    )
                }
            }
        }
    }


    suspend fun fetchState(): String? {
        return try {
            val result = authRepository.fetchState()
            Log.d("AuthViewModel", "Tipo di result: ${result::class.simpleName}")
            if (result is AuthResult.Success && !result.data.isNullOrEmpty()) {
                val state = result.data
                Log.d("AuthViewModel", "Stato recuperato: $state")
                _authState.value = _authState.value.copy(isLoading = false)
                state
            } else {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = "Errore sconosciuto durante il recupero dello stato."
                )
                Log.e("AuthViewModel", "Errore sconosciuto durante il recupero dello stato.")
                null
            }
        } catch (e: Exception) {
            _authState.value = _authState.value.copy(
                isLoading = false,
                errorMessage = e.message ?: "Errore durante il recupero dello stato."
            )
            Log.e("AuthViewModel", "Errore durante il fetch dello stato", e)
            null
        }
    }




//    suspend fun fetchState(): String? {
//
//        return try {
//            // Aggiorna lo stato per indicare che è in corso un'operazione
//            _authState.value = _authState.value.copy(isLoading = true)
//
//            // Chiama il repository per ottenere lo stato dal server
//            val stateResult = authRepository.fetchState()
//
//            // Controlla se lo stato è stato recuperato correttamente
//            if (stateResult is AuthResult.Authorized && !stateResult.data.isNullOrEmpty()) {
//                val state = stateResult.data
//                Log.d("AuthViewModel", "Stato recuperato: $state")
//
//                // Aggiorna lo stato dell'app
//                _authState.value = _authState.value.copy(isLoading = false)
//                state // Restituisce lo stato recuperato
//            } else {
//                // Se non è stato possibile recuperare lo stato
//                _authState.value = _authState.value.copy(
//                    isLoading = false,
//                    errorMessage = "Errore sconosciuto durante il recupero dello stato."
//                )
//                Log.e("AuthViewModel", "Errore sconosciuto durante il recupero dello stato.")
//                null
//            }
//        } catch (e: Exception) {
//            // Gestione degli errori
//            _authState.value = _authState.value.copy(
//                isLoading = false,
//                errorMessage = e.message ?: "Errore durante il recupero dello stato."
//            )
//            Log.e("AuthViewModel", "Errore durante il fetch dello stato", e)
//            null
//        }
//
//    }


}
