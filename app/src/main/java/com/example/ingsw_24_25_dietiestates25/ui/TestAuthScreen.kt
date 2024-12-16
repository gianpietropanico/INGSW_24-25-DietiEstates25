package com.example.ingsw_24_25_dietiestates25.ui
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.* // Per remember, mutableStateOf e delega by
import androidx.compose.ui.platform.LocalContext

import com.example.ingsw_24_25_dietiestates25.data.auth.AuthUiEvent
import com.example.ingsw_24_25_dietiestates25.ui.social.FacebookLoginButton

import com.example.ingsw_24_25_dietiestates25.ui.social.GoogleSignInButton

@Composable
fun LoginAppTest(
    viewModel: AuthViewModel,
    onAuthenticateClicked: () -> Unit
) {

    // Stato per gestire la schermata attuale
    var isLoginScreen by remember { mutableStateOf(true) } // True = Login, False = SignUp
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    // Mostra il Toast
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Schermata principale
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopSection()
            Spacer(modifier = Modifier.height(24.dp))

            // Contenuto dinamico in base allo stato
            if (isLoginScreen) {
                Form(
                    email = authState.signInEmail,
                    password = authState.signInPassword,
                    onEmailChange = { viewModel.onEvent(AuthUiEvent.SignInEmailChanged(it)) },
                    onPasswordChange = { viewModel.onEvent(AuthUiEvent.SignInPasswordChanged(it)) },
                    linkText = "Hai dimenticato la password? Resettala",
                    onClickText = {}, // TODO: Naviga a ResetPasswordScreen
                    onClickButton = {
                        viewModel.onEvent(AuthUiEvent.SignIn)
                        onAuthenticateClicked()
                    },
                    textButton = "LOG IN",
                    isSignUpMode = false // Modalità Login
                )
            } else {
                Form(
                    email = authState.signUpEmail,
                    password = authState.signUpPassword,
                    confirmPassword = authState.signUpConfirmPassword, // Nuovo parametro
                    onEmailChange = { viewModel.onEvent(AuthUiEvent.SignUpEmailChanged(it)) },
                    onPasswordChange = { viewModel.onEvent(AuthUiEvent.SignUpPasswordChanged(it)) },
                    onConfirmPasswordChange = { viewModel.onEvent(AuthUiEvent.SignUpConfirmPasswordChanged(it)) },
                    linkText = "",
                    onClickText = { isLoginScreen = true }, // Torna al login
                    onClickButton = {
                        viewModel.onEvent(AuthUiEvent.SignUp)
                        onAuthenticateClicked()
                    },
                    textButton = "SIGN UP",
                    isSignUpMode = true // Modalità Registrazione
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            SocialLoginSection()
            Spacer(modifier = Modifier.height(24.dp))

            // Link per cambiare schermata
            LinkText(
                linkText = if (isLoginScreen) "Non hai un account? Registrati ora!" else "Hai già un account? Accedi",
                onClick = { isLoginScreen = !isLoginScreen } // Cambia schermata
            )
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


// Sezione per il login tramite piattaforme social
@Composable
private fun SocialLoginSection(context: Context = LocalContext.current) {
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
            GoogleSignInButton( context )
            FacebookLoginButton()
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


