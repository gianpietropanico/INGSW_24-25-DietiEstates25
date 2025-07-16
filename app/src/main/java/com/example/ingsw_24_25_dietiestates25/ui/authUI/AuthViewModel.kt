package com.example.ingsw_24_25_dietiestates25.ui.authUI

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieti_estate.ui.authenticate.AuthState
import com.example.ingsw_24_25_dietiestates25.data.repository.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.result.AuthResult
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

    fun startLogin(context: Context) {
        val clientId = "Ov23liKKZ3TLrVsufnT8"
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)

            val state = fetchState()
            if (state.isNullOrBlank()) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = "Impossibile generare CSRF."
                )
                return@launch
            }

            // Costruisci l’URI con Uri.Builder
            val authUri = Uri.Builder()
                .scheme("https")
                .authority("github.com")
                .appendPath("login")
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("scope", "read:user user:email")
                .appendQueryParameter("state", state)
                .build()

            // Apri Custom Tab con fallback
            try {
                val customTabs = CustomTabsIntent.Builder().build().apply {
                    intent.setPackage("com.android.chrome")
                }
                customTabs.launchUrl(context, authUri)
            } catch (e: Exception) {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, authUri)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }


    fun onOAuthResponse(code: String?, state: String?) {
        if (code.isNullOrBlank() || state.isNullOrBlank()) {
            _authState.value = _authState.value.copy(
                isLoading = false,
                isAuthenticated = false,
                errorMessage = "Autenticazione fallita."
            )
            return
        }

        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)
            val result = authRepository.exchangeGitHubCode(code, state)
            handleResult(result)
        }
    }

    private suspend fun fetchState(): String? =
        runCatching {
            (authRepository.fetchState() as? AuthResult.Success)?.data
                .takeIf { it.isNullOrBlank().not() }
        }.getOrNull()


}
