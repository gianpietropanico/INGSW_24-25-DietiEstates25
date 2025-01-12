package com.example.ingsw_24_25_dietiestates25.ui.social


import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.CallbackManager
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
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.ingsw_24_25_dietiestates25.R

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
            Icon(
                painter = painterResource(id = R.drawable.facebook_icon), // Sostituisci con il nome del tuo file
                contentDescription = "Facebook Icon", // Descrizione per l'accessibilità
                modifier = Modifier.size(60.dp), // Dimensione dell'icona
                tint = Color.Unspecified // Mantieni i colori originali dell'icona
            )
        }
    }