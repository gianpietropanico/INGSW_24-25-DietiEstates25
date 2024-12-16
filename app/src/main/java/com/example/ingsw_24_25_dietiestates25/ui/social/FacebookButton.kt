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
 */@Composable
fun FacebookLoginButton(context: Context = LocalContext.current) {
    val callbackManager = remember { CallbackManager.Factory.create() }

    // Stato per mostrare il login
    var loginResult by remember { mutableStateOf("Non autenticato") }

    // Launcher con ActivityResultContracts per gestire il risultato
    val loginLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        callbackManager.onActivityResult(result.resultCode, result.resultCode, result.data)
    }

    // Callback per il login di Facebook
    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val accessToken = result.accessToken
                loginResult = "Login riuscito: ${accessToken.userId}"
                Log.d("FacebookAuth", "Token: ${accessToken.token}")

                // Logout immediato
                LoginManager.getInstance().logOut()
                Log.d("FacebookAuth", "Logout immediato dopo il login")
            }

            override fun onCancel() {
                loginResult = "Login annullato"
                Log.d("FacebookAuth", "Login annullato")
            }

            override fun onError(error: FacebookException) {
                loginResult = "Errore: ${error.message}"
                Log.e("FacebookAuth", "Errore: ${error.message}")
            }
        })
        onDispose {
            Log.d("FacebookAuth", "Callback unregistered")
        }
    }

    // UI
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = loginResult, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                Log.d("FacebookAuth", "Bottone premuto: Avvio login")
                val intent = LoginManager.getInstance()
                    .logInWithReadPermissions(context as Activity, listOf("public_profile", "email"))
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Accedi con Facebook")
        }
    }
}
