package com.example.ingsw_24_25_dietiestates25

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.* // Per remember, mutableStateOf e delega by
import androidx.compose.ui.text.input.* // Per visualTransformation e KeyboardType


// Funzione principale che rappresenta l'intera schermata di login
@Composable
fun LoginScreen() {
    // Definisce la superficie dell'intera schermata
    Surface(
        modifier = Modifier.fillMaxSize(), // Occupa tutta la dimensione dello schermo
        color = MaterialTheme.colorScheme.background // Usa il colore di sfondo del tema
    ) {
        // Colonna per organizzare i componenti in verticale
        Column(
            modifier = Modifier
                .fillMaxSize() // Riempie lo spazio verticale
                .padding(horizontal = 24.dp), // Aggiunge margini orizzontali
            verticalArrangement = Arrangement.Center, // Centra i contenuti verticalmente
            horizontalAlignment = Alignment.CenterHorizontally // Allinea i contenuti al centro
        ) {
            TopSection() // Sezione superiore con il titolo

            Spacer(modifier = Modifier.height(24.dp)) // Spazio tra sezioni

            LoginForm() // Modulo di login con campi e pulsante

            Spacer(modifier = Modifier.height(16.dp)) // Spazio tra il modulo e il social login

            SocialLoginSection() // Sezione per il login tramite piattaforme social

            Spacer(modifier = Modifier.height(24.dp)) // Spazio tra social login e footer

            FooterSection() // Footer con messaggio di registrazione
        }
    }
}

// Sezione superiore che include il titolo e una linea divisoria
@Composable
private fun TopSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally // Allinea i contenuti al centro
    ) {
        Text(
            text = "DIETIESTATES25", // Testo del titolo principale
            color = MaterialTheme.colorScheme.primary, // Colore primario del tema
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold), // Stile del testo
            textAlign = TextAlign.Center // Centra il testo
        )
        Spacer(modifier = Modifier.height(8.dp)) // Spazio sotto il titolo
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(0.5f), // Occupa metà larghezza dello schermo
            thickness = 1.dp, // Spessore della linea
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f) // Colore semi-trasparente
        )
    }
}

// Modulo di login con campi di input e pulsante
@Composable
private fun LoginForm() {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp), // Spazio uniforme tra gli elementi
        horizontalAlignment = Alignment.CenterHorizontally, // Allinea gli elementi al centro
        modifier = Modifier.fillMaxWidth() // Larghezza massima
    ) {

        LoginTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email Address",
            placeholder = "Inserisci la tua email",
            keyboardType = KeyboardType.Email
        )

        // Campo di testo per la password
        LoginTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password", // Etichetta del campo
            placeholder = "Inserisci la tua password", // Testo segnaposto
            keyboardType = KeyboardType.Password, // Configurazione della tastiera per password
            isPassword = true // Abilita la trasformazione della password
        )
        // Testo per "password dimenticata"
        Text(
            text = "Did you forget your password?", // Testo del link
            color = MaterialTheme.colorScheme.primary, // Colore del testo
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), // Stile del testo
            modifier = Modifier.align(Alignment.End) // Allinea il testo a destra
        )

        // Pulsante di login
        LoginButton(
            text = "LOG IN", // Testo del pulsante
            onClick = {
                loginError = "" // Reset dell'errore
                if (email.isBlank() || password.isBlank()) {
                    loginError = "I campi email e password sono obbligatori!"
                } else {
                    // Aggiungi qui la logica del login (es. verifica credenziali)
                    if (email == "admin@example.com" && password == "password123") {
                        loginError = "Login riuscito!"
                    }
                    /*else {
                        loginError = "Email o password errati!"
                    }*/
                }
            },

        )

        // Mostra il messaggio di errore se presente
        if (loginError.isNotEmpty()) {
            Text(
                text = loginError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


// Sezione per il login tramite piattaforme social
@Composable
private fun SocialLoginSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp), // Spazio tra gli elementi
        horizontalAlignment = Alignment.CenterHorizontally // Allinea gli elementi al centro
    ) {
        Text(
            text = "OR USE", // Testo divisorio
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), // Stile del testo
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f) // Colore semi-trasparente
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp), // Spazio tra i pulsanti
            verticalAlignment = Alignment.CenterVertically // Allinea verticalmente i pulsanti
        ) {
            SocialButton("Google") // Pulsante per Google
            SocialButton("Facebook") // Pulsante per Facebook
            SocialButton("GitHub") // Pulsante per GitHub
        }
    }
}

// Pulsante generico per social login
@Composable
private fun SocialButton(platform: String) {
    OutlinedButton(
        onClick = { /* Aggiungi logica per il social login */ }, // Callback per il click
        modifier = Modifier
            .height(40.dp) // Altezza del pulsante

    ) {
        Text(
            text = platform, // Nome della piattaforma
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) // Stile del testo
        )
    }
}

// Footer con link per registrazione
@Composable
private fun FooterSection() {
    Text(
        text = "Not a member? Register now!", // Testo del footer
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp), // Stile del testo
        textAlign = TextAlign.Center, // Centra il testo
        color = MaterialTheme.colorScheme.primary, // Colore del testo
        modifier = Modifier.fillMaxWidth() // Larghezza massima
    )
}
