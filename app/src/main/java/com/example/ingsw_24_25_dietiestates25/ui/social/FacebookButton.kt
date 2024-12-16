package com.example.ingsw_24_25_dietiestates25.ui.social


import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.CallbackManager
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember


import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*

import androidx.compose.ui.Alignment

/*
    OutlinedButton(
        onClick = {
            Log.d("FacebookButton", "Bottone premuto, avvio del flusso di login")
            LoginManager.getInstance()
                .logInWithReadPermissions(context as Activity, listOf("email", "public_profile"))
        },
        modifier = Modifier.height(40.dp)
    ) {
        Text(
            text = "Facebook",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
 */
@Composable
fun FacebookLoginButton(
    context: Context = LocalContext.current,
    autoLogout: Boolean = false // Parametro per abilitare/disabilitare il logout immediato
) {
    val callbackManager = remember { CallbackManager.Factory.create() }
    var loginResult by remember { mutableStateOf("Non autenticato") }

    // Launcher per il login
    val loginLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        callbackManager.onActivityResult(result.resultCode, result.resultCode, result.data)
    }

    // Registrazione dei callback
    DisposableEffect(Unit) {
        val loginManager = LoginManager.getInstance()
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val accessToken = result.accessToken
                loginResult = "Login riuscito: ${accessToken.userId}"

                Log.d("FacebookAuth", "Token: ${accessToken.token}")
                Log.d("FacebookAuth", "UserID: ${accessToken.userId}")
                Log.d("FacebookAuth", "Permessi: ${accessToken.permissions}")

                if (autoLogout) {
                    loginManager.logOut()
                    Log.d("FacebookAuth", "Logout immediato dopo il login")
                }
            }

            override fun onCancel() {
                loginResult = "Login annullato"
                Log.d("FacebookAuth", "Login annullato dall'utente")
            }

            override fun onError(error: FacebookException) {
                loginResult = "Errore durante il login: ${error.message}"
                Log.e("FacebookAuth", "Errore durante il login", error)
            }
        })

        onDispose {
            loginManager.unregisterCallback(callbackManager)
            Log.d("FacebookAuth", "Callback unregistered")
        }
    }

        // Pulsante di login
        OutlinedButton(
            onClick = {
                Log.d("FacebookAuth", "Bottone premuto: Avvio login")
                LoginManager.getInstance().logInWithReadPermissions(
                    context as Activity,
                    listOf("public_profile", "email")
                )
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Facebook",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
/*
 fun onSuccess(result: LoginResult) {
    val accessToken = result.accessToken
    loginResult = "Login riuscito: ${accessToken.userId}"

    Log.d("FacebookAuth", "Token: ${accessToken.token}")
    Log.d("FacebookAuth", "UserID: ${accessToken.userId}")
    Log.d("FacebookAuth", "Permessi: ${accessToken.permissions}")

    // Ottenere i dati dell'utente tramite API Graph
    val request = GraphRequest.newMeRequest(accessToken) { jsonObject, response ->
        try {
            val name = jsonObject.getString("name")
            val email = jsonObject.optString("email", "Email non disponibile")
            val profilePicUrl = jsonObject.getJSONObject("picture")
                .getJSONObject("data")
                .getString("url")

            Log.d("FacebookUserData", "Nome: $name")
            Log.d("FacebookUserData", "Email: $email")
            Log.d("FacebookUserData", "Foto Profilo: $profilePicUrl")

            // Aggiorna lo stato per mostrare i dati
            loginResult = "Login riuscito: $name ($email)"
        } catch (e: Exception) {
            Log.e("FacebookUserData", "Errore nel parsing dei dati dell'utente", e)
        }
    }

    val parameters = Bundle()
    parameters.putString("fields", "id,name,email,picture.type(large)")
    request.parameters = parameters
    request.executeAsync()

    if (autoLogout) {
        LoginManager.getInstance().logOut()
        Log.d("FacebookAuth", "Logout immediato dopo il login")
    }
}
*/