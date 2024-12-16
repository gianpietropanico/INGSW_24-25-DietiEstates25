package com.example.ingsw_24_25_dietiestates25.ui.social

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FacebookButton(
    context: Context = LocalContext.current
) {

    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val launcher = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {
        Log.d("FacebookButton", "ActivityResult ricevuto, il flusso è stato completato")
    }

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                Log.d("FacebookButton", "Login annullato dall'utente")
            }

            override fun onSuccess(result: LoginResult) {
                val accessToken = result.accessToken
                Log.d("FacebookAuth", "Token di accesso: ${accessToken.token}")
                Log.d("FacebookAuth", "UserID: ${accessToken.userId}")
                Log.d("FacebookAuth", "Permessi: ${accessToken.permissions}")

            }

            override fun onError(error: FacebookException) {
                Log.e("FacebookAuth", "Errore durante il login: ${error.message}")
                error.printStackTrace()

            }

        })

        onDispose {
            loginManager.unregisterCallback(callbackManager)
            Log.d("FacebookButton", "Callback di Facebook unregistrato")
        }
    }

    OutlinedButton(
            onClick = {
                Log.d("FacebookButton", "Bottone premuto, avvio del flusso di login")
                launcher.launch(listOf("email", "public_profile"))
            },
        modifier = Modifier
            .height(40.dp) // Altezza del pulsante

    ) {
        Text(
            text = "Facebook", // Nome della piattaforma
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) // Stile del testo
        )
    }

}
