package com.example.ingsw_24_25_dietiestates25.ui.social

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import com.example.ingsw_24_25_dietiestates25.data.model.User
import kotlinx.coroutines.launch

/* TODO
Validazione del token JWT:

Server: Hai bisogno di endpoint protetti? Assicurati di validare il token JWT per accedere a risorse riservate.
Client: Dovresti verificare se il token JWT è valido o scaduto per aggiornare lo stato di autenticazione.

NON VIENE ESEGUITO IL REGISTERCALLBACK DI GITHUBCALLBACKMANAGER
*/
@Composable
fun GitHubButton(
    fetchState: suspend () -> String?,
    notifyServer: suspend (String, String) -> User?,
    context: Context = LocalContext.current,
    onSucces: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var state by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    OutlinedButton(
        onClick = {
            isLoading = true
            resultMessage = null // Reset error messages

            Log.d("GitHubButton", "Button clicked, starting state fetch.")

            coroutineScope.launch {
                try {
                    state = fetchState() // Fetch the state (CSRF token)
                    if (!state.isNullOrBlank()) {
                        Log.d("GitHubButton", "State successfully fetched: $state")
                        openGitHubOAuth(context, state!!)
                    } else {
                        resultMessage = "Failed to fetch state."
                        Log.e("GitHubButton", "State is null or blank.")
                    }
                } catch (e: Exception) {
                    Log.e("GitHubButton", "Error fetching state: ${e.message}", e)
                    resultMessage = "An error occurred."
                } finally {
                    isLoading = false
                    Log.d("GitHubButton", "State fetch process completed.")
                }
            }
        },
        enabled = !isLoading,
        modifier = Modifier.padding(8.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
        } else {
            Text("GitHub")
        }
    }

    GitHubCallbackManager.registerCallback { code, receivedState ->
        Log.d("GitHubButton", "Callback received with code: $code and state: $receivedState")

        if (!code.isNullOrBlank() && !receivedState.isNullOrBlank()) {
            coroutineScope.launch {
                try {
                    isLoading = true
                    Log.d("GitHubButton", "Exchanging code for token with code: $code and state: $receivedState")

                    val user = notifyServer(code, receivedState)
                    if (user != null) {
                        resultMessage = "Authentication successful!"
                        Log.d("GitHubButton", "User Email: ${user.email}, Username: ${user.username} ")
                        onSucces()
                    } else {
                        resultMessage = "Failed to exchange code for token."
                        Log.e("GitHubButton", "Access token is null or blank.")
                    }
                } catch (e: Exception) {
                    Log.e("GitHubButton", "Error exchanging code: ${e.message}", e)
                    resultMessage = "An error occurred during token exchange."
                } finally {
                    isLoading = false
                    Log.d("GitHubButton", "Token exchange process completed.")
                }
            }
        } else {
            resultMessage = "Authentication failed: missing code or state."
            Log.e("GitHubButton", "Code or state is null or blank in callback.")
        }
    }

    resultMessage?.let {
        Spacer(modifier = Modifier.height(16.dp))
        Text(it, color = MaterialTheme.colorScheme.error)
    }
}

fun openGitHubOAuth(context: Context, state: String) {
    val clientId = "Ov23liKKZ3TLrVsufnT8" // Replace with your GitHub Client ID
    val authUrl = "https://github.com/login/oauth/authorize?" +
            "client_id=$clientId&scope=read:user,user:email&state=$state"

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



//
//@Composable
//fun GitHubButton(
//    fetchState: suspend () -> String?,
//) {
//    val context = LocalContext.current
//    var isLoading by remember { mutableStateOf(false) }
//    var resultMessage by remember { mutableStateOf<String?>(null) }
//    var state by remember { mutableStateOf<String?>(null) }
//    OutlinedButton(
//        onClick = {
//            isLoading = true
//            resultMessage = null // Resetta eventuali errori
//
//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    state = fetchState() // Chiamata alla funzione suspend
//                    if (state != null) { // Stato recuperato correttamente
//                        withContext(Dispatchers.Main) {
//                            Log.d("GitHubButton", "Stato recuperato con successo: $state")
//                            // Apri GitHub OAuth
//                            openGitHubOAuth(context, state)
//                        }
//                    } else { // Stato nullo
//                        withContext(Dispatchers.Main) {
//                            Log.e("GitHubButton", "Lo stato recuperato è nullo")
//                            resultMessage = "Errore durante il recupero dello stato."
//                        }
//                    }
//                } catch (e: Exception) {
//                    withContext(Dispatchers.Main) {
//                        resultMessage = "Errore durante la generazione dello stato."
//                        Log.e("GitHubButton", "Errore: ${e.message}")
//                    }
//                } finally {
//                    isLoading = false
//                }
//            }
//        },
//        enabled = !isLoading,
//        modifier = Modifier.padding(8.dp)
//    ) {
//        if (isLoading) {
//            CircularProgressIndicator(modifier = Modifier.size(16.dp))
//        } else {
//            Text(
//                text = "GitHub",
//                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
//            )
//        }
//    }
//
//
//    // Mostra eventuali errori
//    resultMessage?.let {
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(it, color = MaterialTheme.colorScheme.error)
//    }
//}