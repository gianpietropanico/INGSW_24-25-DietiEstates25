package com.example.ingsw_24_25_dietiestates25

import android.content.Intent

import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ingsw_24_25_dietiestates25.ui.AgencyRegistrationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainScreen() }
    }

}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {
        AgencyRegistrationScreen()
    }
}



/*
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data?.let { uri ->
            // Controlla se l'intent contiene il parametro "code"
            val code = uri.getQueryParameter("code")
            if (code != null) {
                println("Codice di autorizzazione ricevuto: $code")
                // Invia il codice al server
                authenticateWithServer(code)
            } else {
                println("Errore: Codice non trovato nel redirect.")
            }
        }
    }
}
private fun authenticateWithServer(code: String) {
    // Qui puoi implementare la logica per inviare il codice al server
    println("Invio del codice al server: $code")
    // Puoi anche mostrare un caricamento o aggiornare lo stato della UI
}

@Composable
fun GitHubLoginScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Benvenuto!", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                openGitHubOAuth(context)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Accedi con GitHub")
        }
    }
}


fun openGitHubOAuth(context: Context) {
    val authUrl = "http://10.0.2.2:8080/login/github" // URL del server locale
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
    context.startActivity(intent)
}
*/