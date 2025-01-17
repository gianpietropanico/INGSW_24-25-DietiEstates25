package com.example.ingsw_24_25_dietiestates25.ui.social


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log


import androidx.compose.foundation.layout.*


import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme


import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*

import androidx.compose.material3.Text


import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import com.example.ingsw_24_25_dietiestates25.R
import com.facebook.login.LoginManager

@Composable
fun GitHubButton() {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    // Pulsante di login

    Box(
        modifier = Modifier
            .padding(8.dp) // Margine esterno
            .size(60.dp) // Dimensione totale del pulsante
            .clickable {
                isLoading = true
                openGitHubOAuth(context) { code ->
                    if (code != null) {
                        // Invia il codice al server
                        authenticateWithServer(code) { result ->
                            isLoading = false
                            resultMessage = result
                        }
                    } else {
                        isLoading = false
                        resultMessage = "Errore: Codice non trovato nel redirect."
                    }
                }
            }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.github), // Icona di GitHub
            contentDescription = "GitHub Icon", // Descrizione per l'accessibilità
            modifier = Modifier.fillMaxSize(), // L'icona occupa tutto lo spazio disponibile
            tint = Color.Unspecified // Mantieni i colori originali dell'icona
        )
    }


    resultMessage?.let {
        Spacer(modifier = Modifier.height(16.dp))
        Text(it)
    }
}

fun openGitHubOAuth(context: Context, onCodeReceived: (String?) -> Unit) {
    val authUrl = "http://10.0.2.2:8080/login/github"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
    context.startActivity(intent)

    // Gestisce il redirect
    val activity = context as? ComponentActivity
    activity?.let {
        it.intent?.data?.let { uri ->
            val code = uri.getQueryParameter("code")
            onCodeReceived(code)
        }
    }
}

fun authenticateWithServer(code: String, onResult: (String) -> Unit) {
    // Simula una chiamata al server (puoi usare una libreria come Ktor o Retrofit qui)
    println("Invio del codice al server: $code")
    // Mock del risultato
    onResult("Autenticazione completata con successo per il codice: $code")
}


