package com.example.ingsw_24_25_dietiestates25.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.* // Per remember, mutableStateOf e delega by
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthUiEvent

@Composable
fun LoginScreen(viewModel: AuthViewModel) {
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    // Mostra il Toast
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(32.dp), // Spaziatura verticale tra i moduli
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Form(
            email = authState.signUpEmail,
            password = authState.signUpPassword,
            onEmailChange = { viewModel.onEvent(AuthUiEvent.SignUpEmailChanged(it)) },
            onPasswordChange = { viewModel.onEvent(AuthUiEvent.SignUpPasswordChanged(it)) },
            onClick = { viewModel.onEvent(AuthUiEvent.SignUp) },
            textButton = "SIGN IN"
        )

        Form(
            email = authState.signInEmail,
            password = authState.signInPassword,
            onEmailChange = { viewModel.onEvent(AuthUiEvent.SignInEmailChanged(it)) },
            onPasswordChange = { viewModel.onEvent(AuthUiEvent.SignInPasswordChanged(it)) },
            onClick = { viewModel.onEvent(AuthUiEvent.SignIn) },
            textButton = "LOG IN"
        )
    }
}


@Composable
private fun Form(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onClick: () -> Unit,
    textButton : String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        LoginTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email Address",
            placeholder = "Inserisci la tua email",
            keyboardType = KeyboardType.Email
        )

        LoginTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            placeholder = "Inserisci la tua password",
            keyboardType = KeyboardType.Password,
            isPassword = true
        )

        LoginButton(
            text = textButton,
            onClick = onClick
        )
    }
}

