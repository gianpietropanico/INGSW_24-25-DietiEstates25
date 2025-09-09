package com.example.ingsw_24_25_dietiestates25.ui.authUI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.repository.authRepo.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.result.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultRegistryOwner

import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.CallbackManager

import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepository
import com.example.ingsw_24_25_dietiestates25.model.state.AuthState
import com.example.ingsw_24_25_dietiestates25.ui.utils.downloadImageAsBase64
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import org.json.JSONObject
import java.security.MessageDigest
import java.util.UUID


import com.facebook.GraphRequest



@HiltViewModel
class AuthViewModel @Inject constructor (
    private val authRepository: AuthRepository,
    userSessionManager: UserSessionManager,
    private val imageRepository: ImageRepository
) : ViewModel() {

    val user = userSessionManager.currentUser

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun signUpUser(email: String, password: String, defaultProfilePic: String) {
        clearResultMessage()

        if ( password == _authState.value.confirmPassword) {

            viewModelScope.launch {
                _authState.update { it.copy(isLoading = true, resultMessage = null) }

                var result = authRepository.signUp(email, password, defaultProfilePic)

                if (result is AuthResult.Authorized) {

                    result = authRepository.getLoggedUser()

                    if ( result is  AuthResult.Authorized)
                        imageRepository.insertProfilePicture(user.value!!.id,defaultProfilePic )
                }

                handleResult(result)
            }

        } else {
            _authState.update { it.copy(isLoading = false, resultMessage = "Le password non combaciano", localError = true) }
        }
    }

    fun signInUser(email: String, password: String) {
        clearResultMessage()

        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, resultMessage = null) }
            var result = authRepository.signIn(email, password)

            if (result is AuthResult.Authorized) {

                result = authRepository.getLoggedUser()

                if ( result is  AuthResult.Authorized)
                    imageRepository.getImage(user.value!!.id)
            }

            handleResult(result)
        }
    }

    fun authWithThirdParty(email : String?, username :String?, defaultProfilePic: String) {
        clearResultMessage()
        if ( email != null && username != null ) {

            viewModelScope.launch {
                _authState.update { it.copy(isLoading = true, resultMessage = null) }

                var result = authRepository.authWithThirdParty(email, username)

                if (result is AuthResult.Authorized) {

                    result = authRepository.getLoggedUser()

                    if( user.value?.profilePicture == null){

                        imageRepository.insertProfilePicture(user.value!!.id, defaultProfilePic )

                    }else{
                        imageRepository.getImage(user.value!!.id)
                    }

                }

                handleResult(result)
            }

        } else {
            _authState.update { it.copy(isLoading = false, resultMessage = "Problema con OUATH ", localError = true) }
        }
    }



    private fun handleResult(result: AuthResult<Unit>) {
        when (result) {
            is AuthResult.Authorized -> {
                _authState.update{ it.copy(isLoading = false,isAuthenticated = true,resultMessage = null,localError = false)}
            }

            is AuthResult.Unauthorized -> {

                _authState.update { it.copy(isLoading = false, resultMessage = result.message, isAuthenticated = false, localError = true) }

            }

            is AuthResult.UnknownError -> {
                _authState.update { it.copy(isLoading = false, resultMessage = result.message, isAuthenticated = false, localError = true) }
            }

            is AuthResult.Success -> {
                _authState.update { it.copy(isLoading = false, resultMessage = result.message, isAuthenticated = false, success = true) }
            }
        }
    }


    fun clearResultMessage() {
        _authState.update { it.copy(resultMessage = null, success = false, localError = false) }
    }

    fun unAuthorized( message : String){
        _authState.update { it.copy(isLoading = false, resultMessage = message, isAuthenticated = false) }
    }

    fun isLoading( ) : Boolean{
        return _authState.value.isLoading
    }

    fun startGithubLogin(context: Context) {
        val clientId = "Ov23liKKZ3TLrVsufnT8"
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, resultMessage = null, isAuthenticated = false) }

            val state = fetchState()
            if (state.isNullOrBlank()) {
                _authState.update { it.copy(isLoading = false, resultMessage = "Impossibile generare CRSF", isAuthenticated = false) }
                return@launch
            }

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

            _authState.update { it.copy(isLoading = false, resultMessage = "Autenticazione fallita.", isAuthenticated = false) }

            return
        }

        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, resultMessage = null) }
            val result = authRepository.exchangeGitHubCode(code, state)
            handleResult(result)
        }
    }

    private suspend fun fetchState(): String? =
        runCatching {
            (authRepository.fetchState() as? AuthResult.Success)?.data
                .takeIf { it.isNullOrBlank().not() }
        }.getOrNull()

    fun sendAgencyRequest(agencyName: String , email: String , password: String) {

        clearResultMessage()

        if ( password == _authState.value.confirmPassword) {

            viewModelScope.launch {
                _authState.update { it.copy(isLoading = true, resultMessage = null) }
                val result = authRepository.sendAgencyRequest(email, password, agencyName)
                handleResult(result)
            }
        } else {
            _authState.update { it.copy(isLoading = false, resultMessage = "Le password non combaciano", localError = true) }
        }

    }

    fun startFacebookLogin(activity: Activity) {
        val callbackManager = CallbackManager.Factory.create()
        val loginManager = LoginManager.getInstance()

        _authState.update { it.copy(isLoading = true, resultMessage = null) }

        loginManager.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d("FacebookLogin", "Login cancelled.")
                    unAuthorized("Operazione annullata")
                }

                override fun onError(error: FacebookException) {
                    Log.e("FacebookLogin", "Login error: ${error.message}")
                    unAuthorized("Errore durante il login con Facebook")
                }
                override fun onSuccess(result: LoginResult) {
                    GraphRequest.newMeRequest(result.accessToken) { obj, _ ->
                        val email = obj?.getString("email") ?: "Email non disponibile"
                        val name = obj?.getString("name") ?: "Nome non disponibile"
                        val profilePic = obj
                            ?.getJSONObject("picture")
                            ?.getJSONObject("data")
                            ?.getString("url") ?: ""

                        Log.d("FacebookLogin", "Login success. Email: $email, Name: $name")

                        viewModelScope.launch {
                            val base64 = downloadImageAsBase64(profilePic)
                            authWithThirdParty(email, name, base64!!)
                        }

                    }.apply {
                        parameters = Bundle().apply {
                            putString("fields", "id,name,email,picture.type(large)")
                        }
                        executeAsync()
                    }
                }
            }
        )

        val registryOwner = activity as? ActivityResultRegistryOwner
        if (registryOwner != null) {
            loginManager.logIn(
                registryOwner,
                callbackManager,
                listOf("email", "public_profile")
            )
        } else {
            Log.e("FacebookLogin", "Activity does not implement ActivityResultRegistryOwner")
            unAuthorized("Errore interno: activity incompatibile")
        }
    }

    fun startGoogleLogin(context: Context) {
        val tag = "GoogleSignIn"
        val credentialManager = CredentialManager.create(context)

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        val hashedNonce = digest.joinToString("") { "%02x".format(it) }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("747472826480-gu5faa67ev4uocejm52tscmo1bjecq0h.apps.googleusercontent.com")
            .setNonce(hashedNonce)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, resultMessage = null) }

            try {
                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                val credential = result.credential
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                Log.i(tag, idToken)

                val segments = idToken.split(".")
                val payloadAsByteArray =
                    android.util.Base64.decode(segments[1], android.util.Base64.NO_PADDING)
                val payloadInJson = JSONObject(String(payloadAsByteArray, Charsets.UTF_8))

                val email = payloadInJson.getString("email")
                val name = payloadInJson.getString("name")
                val base64Image = downloadImageAsBase64(payloadInJson.getString("picture"))

                authWithThirdParty(email, name, base64Image!!)

            } catch (e: Exception) {
                Log.e(tag, "Login failed: ${e.message}")
                unAuthorized("Errore durante il login con Google")
            }
        }
    }

    fun clearState() {
        _authState.update{ it.copy(isLoading = false,isAuthenticated = false,resultMessage = null,localError = false)}
    }

}
