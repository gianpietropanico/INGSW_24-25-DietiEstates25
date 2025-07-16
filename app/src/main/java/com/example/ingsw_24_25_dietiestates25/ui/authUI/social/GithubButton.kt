package com.example.ingsw_24_25_dietiestates25.ui.authUI.social

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.authUI.AuthViewModel

/* TODO NOTARE LA VARIABILE IS LOADING , UTILE PER INDICARE CHE IL PULSANTE è STATO
*   PREMUTO E CHE STA ESEGUENDO I PROCESSI DEL ONCLICK BUTTON*/
/*
    OutlinedButton(
        onClick = {
            isLoading = true,

            FUNZIONE PER IL CLICK()

        enabled = !isLoading,
        modifier = Modifier.padding(8.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
        } else {
            Text("GitHub")
        }
    }

    FUNZIONE PER IL CLICK(){
        PROCESSI DELLA FUNZIONE
        ....
        isLoading = false
        ...
    }

    it IN QUESTO CASO INDICA LA CIRCULAR PROGRESSION E IL TESTO "github"
    Text(it, color = MaterialTheme.colorScheme.error)
*/


@Composable
fun GitHubButton(
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    val uiState by viewModel.authState.collectAsState()

    // Registriamo una volta sola il deep-link callback
    LaunchedEffect(Unit) {
        GitHubCallbackManager.register { code, state ->
            viewModel.onOAuthResponse(code, state)
        }
    }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(60.dp)
            .clickable(enabled = !uiState.isLoading) {
                viewModel.startLogin(context)
            }
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
        } else {
            Icon(
                painter = painterResource(R.drawable.github),
                contentDescription = "Login with GitHub",
                modifier = Modifier.fillMaxSize(),
                tint = Color.Unspecified
            )
        }
    }

}





//@Composable
//fun GitHubButton(
//    fetchState: suspend () -> String?,
//    notifyServer: suspend (String, String) -> User?,
//    context: Context = LocalContext.current,
//    onSucces: () -> Unit
//) {
//    var isLoading by remember { mutableStateOf(false) }
//    var resultMessage by remember { mutableStateOf<String?>(null) }
//    var state by remember { mutableStateOf<String?>(null) }
//
//    val coroutineScope = rememberCoroutineScope()
//
//    Box(
//        modifier = Modifier
//            .padding(8.dp) // Margine esterno
//            .size(60.dp) // Dimensione totale del pulsante
//            .clickable(
//                enabled = !isLoading
//            ) {
//                isLoading = true
//                resultMessage = null // Reset error messages
//
//                Log.d("GitHubButton", "Button clicked, starting state fetch.")
//
//                coroutineScope.launch {
//                    try {
//                        state = fetchState() // Fetch the state (CSRF token)
//                        if (!state.isNullOrBlank()) {
//                            Log.d("GitHubButton", "State successfully fetched: $state")
//                            openGitHubOAuth(context, state!!)
//                        } else {
//                            resultMessage = "Failed to fetch state."
//                            Log.e("GitHubButton", "State is null or blank.")
//                        }
//                    } catch (e: Exception) {
//                        Log.e("GitHubButton", "Error fetching state: ${e.message}", e)
//                        resultMessage = "An error occurred."
//                    } finally {
//                        isLoading = false
//                        Log.d("GitHubButton", "State fetch process completed.")
//                    }
//                }
//            },
//        //modifier = Modifier.padding(8.dp)
//    ) {
//        if (isLoading) {
//            CircularProgressIndicator(modifier = Modifier.size(16.dp))
//        } else {
//            Text("GitHub")
//        }
//    }
//
//    GitHubCallbackManager.registerCallback { code, receivedState ->
//        Log.d("GitHubButton", "Callback received with code: $code and state: $receivedState")
//
//        if (!code.isNullOrBlank() && !receivedState.isNullOrBlank()) {
//            coroutineScope.launch {
//                try {
//                    isLoading = true
//                    Log.d("GitHubButton", "Exchanging code for token with code: $code and state: $receivedState")
//
//                    val user = notifyServer(code, receivedState)
//                    if (user != null) {
//                        resultMessage = "Authentication successful!"
//                        Log.d("GitHubButton", "User Email: ${user.email}, Username: ${user.username} ")
//                        onSucces()
//                    } else {
//                        resultMessage = "Failed to exchange code for token."
//                        Log.e("GitHubButton", "Access token is null or blank.")
//                    }
//                } catch (e: Exception) {
//                    Log.e("GitHubButton", "Error exchanging code: ${e.message}", e)
//                    resultMessage = "An error occurred during token exchange."
//                } finally {
//                    isLoading = false
//                    Log.d("GitHubButton", "Token exchange process completed.")
//                }
//            }
//        } else {
//            resultMessage = "Authentication failed: missing code or state."
//            Log.e("GitHubButton", "Code or state is null or blank in callback.")
//        }
//    }
//
//    resultMessage?.let {
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(it, color = MaterialTheme.colorScheme.error)
//    }
//}