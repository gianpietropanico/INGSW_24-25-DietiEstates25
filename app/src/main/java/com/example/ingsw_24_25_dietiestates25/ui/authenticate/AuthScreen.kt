package com.example.ingsw_24_25_dietiestates25.ui.authenticate
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
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.* // Per remember, mutableStateOf e delega by
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dieti_estate.ui.authenticate.AuthEvent
import com.example.ingsw_24_25_dietiestates25.R

import com.example.ingsw_24_25_dietiestates25.ui.components.social.FacebookLoginButton
import com.example.ingsw_24_25_dietiestates25.ui.components.social.GitHubButton


import com.example.ingsw_24_25_dietiestates25.ui.components.social.GoogleSignInButton
import com.example.ingsw_24_25_dietiestates25.ui.components.common.Form
import com.example.ingsw_24_25_dietiestates25.ui.components.common.LinkText

@Composable
fun AuthScreen(
    am: AuthViewModel = hiltViewModel(),
    navController: NavController
) {

    // Stato per gestire la schermata attuale
    var isLoginScreen by remember { mutableStateOf(true) } // True = Login, False = SignUp
    val authState by am.authState.collectAsState()
    val context = LocalContext.current

    // Mostra il Toast
    LaunchedEffect(Unit) {
        am.toastMessage.collect { message ->
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
                    onEmailChange = { am.onEvent(AuthEvent.SignInEmailChanged(it)) },
                    onPasswordChange = { am.onEvent(AuthEvent.SignInPasswordChanged(it)) },
                    linkText = "Did you forget your password? ",
                    onClickText = {}, // TODO: Naviga a ResetPasswordScreen
                    onClickButton = {
                        am.onEvent(AuthEvent.SignIn)
                    },
                    textButton = "LOG IN",
                    isSignUpMode = false // Modalità Login
                )
            } else {
                Form(
                    email = authState.signUpEmail,
                    password = authState.signUpPassword,
                    confirmPassword = authState.signUpConfirmPassword, // Nuovo parametro
                    onEmailChange = { am.onEvent(AuthEvent.SignUpEmailChanged(it)) },
                    onPasswordChange = { am.onEvent(AuthEvent.SignUpPasswordChanged(it)) },
                    onConfirmPasswordChange = { am.onEvent(AuthEvent.SignUpConfirmPasswordChanged(it)) },
                    linkText = "",
                    onClickText = { isLoginScreen = true }, // Torna al login
                    onClickButton = {
                        am.onEvent(AuthEvent.SignUp)
                    },
                    textButton = "SIGN UP",
                    isSignUpMode = true // Modalità Registrazione
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            SocialLoginSection(
                navController = navController,
                authViewModel = am,
                context = context
                /*TODO
                   QUESTA FUNZIONE DOVREBBE SALVARE L'USER NEL VIEWMODEL
                   IL VIEWMODEL VIENE USATO COME "MULO" PER TRASPORTARE
                   DATI DA UNO SCREEN AD UN ALTRO

                onGitHubLogin = { code ->
                    viewModel.onEvent(AuthUiEvent.GitHubLogin(code))
                }*/
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Link per cambiare schermata
            LinkText(
                linkText = if (isLoginScreen) "Not a member? Register now!" else "Already a member ? Login ",
                fontSize = 24.sp,
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
    ) {/*
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
        */Spacer(modifier = Modifier.height(50.dp))

        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Icona personalizzata",
            tint = Color.Unspecified
        )
    }
}


@Composable
private fun SocialLoginSection(
    authViewModel: AuthViewModel,
    navController: NavController,
    context: Context = LocalContext.current
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp), // Spazio tra gli elementi
        horizontalAlignment = Alignment.CenterHorizontally // Allinea gli elementi al centro

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, // Allinea verticalmente il testo e i divider
            horizontalArrangement = Arrangement.Center // Centra il contenuto orizzontalmente
        ) {
            Spacer(modifier = Modifier.height(44.dp))
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f) // Occupa spazio in modo proporzionale a sinistra
                    .height(1.dp), // Spessore del divider
                //color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f) // Colore semi-trasparente
                color = Color.Black //Colore semi-trasparente
            )
            Text(
                text = "OR USE", // Testo divisorio
                modifier = Modifier.padding(horizontal = 8.dp), // Spazio orizzontale intorno al testo
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 24.sp), // Stile del testo
                color = Color.Black // Colore semi-trasparente


            )
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f) // Occupa spazio in modo proporzionale a destra
                    .height(1.dp), // Spessore del divider
                color = Color.Black
            )
        }


        Row(
            horizontalArrangement = Arrangement.spacedBy(44.dp), // Spazio tra i pulsanti
            verticalAlignment = Alignment.CenterVertically // Allinea verticalmente i pulsanti
        ) {

            GoogleSignInButton(context)
            FacebookLoginButton()
            GitHubButton(
                fetchState = {
                    authViewModel.fetchState() // Chiamata diretta alla funzione suspend
                },
                notifyServer = { code, state ->
                    authViewModel.notifyServer(code, state) // Chiamata diretta alla funzione suspend per scambiare il codice
                },
                onSucces = {  }
            )


        }
        Spacer(modifier = Modifier.width(16.dp))
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


