package com.example.ingsw_24_25_dietiestates25.packagetest

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ingsw_24_25_dietiestates25.AuthUiEvent
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthResult

import kotlinx.coroutines.flow.collect
/*TODO CAPIRE COME SI NAVIGA FRA GLI SCHERMI E COME FUNZIONA HILT MODEL
*  LUI UTILIZZA UN NAVIGATOR DI UN TIPO SU GITHUB CAZZ NE SO
* INOLTRE C'è UNA VIEW MODEL CHE SI CHIAMA HILTEDVIEW MODEL CHE NON HO CAPITO BENE COSA'è
* FACCIO COMMIT E POI SASERA CERCO DI VEDE DI RISOLVE*/


@Composable
fun AuthScreen(
    viewModel: MainViewModel = viewModel(),
    navigator: NavigationAuth = NavigationAuth() // Default navigator
) {
    val state = viewModel.state
    val context = LocalContext.current

    // Effetti collaterali per i risultati di autenticazione
    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { result ->
            handleAuthResult(result, navigator, context)
        }
    }

    // Layout principale
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sezione di Sign Up
            AuthInputFields(
                username = state.signUpEmail,
                onUsernameChange = { viewModel.onEvent(AuthUiEvent.SignUpUsernameChanged(it)) },
                password = state.signUpPassword,
                onPasswordChange = { viewModel.onEvent(AuthUiEvent.SignUpPasswordChanged(it)) },
                onSubmit = { viewModel.onEvent(AuthUiEvent.SignUp) },
                buttonText = "Sign Up"
            )
            Spacer(modifier = Modifier.height(64.dp))

            // Sezione di Sign In
            AuthInputFields(
                username = state.signInEmail,
                onUsernameChange = { viewModel.onEvent(AuthUiEvent.SignInUsernameChanged(it)) },
                password = state.signInPassword,
                onPasswordChange = { viewModel.onEvent(AuthUiEvent.SignInPasswordChanged(it)) },
                onSubmit = { viewModel.onEvent(AuthUiEvent.SignIn) },
                buttonText = "Sign In"
            )
        }

        // Indicatore di caricamento
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xAAFFFFFF)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun AuthInputFields(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    buttonText: String
) {
    Column {
        TextField(
            value = username,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Username") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSubmit,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = buttonText)
        }
    }
}

fun handleAuthResult(
    result: AuthResult<*>,
    navigator: NavigationAuth,
    context: android.content.Context
) {
    when (result) {
        is AuthResult.Authorized<*> -> {
            navigator.navigate(ScreenRoutes.SecretScreen.route) {
                popUpTo(ScreenRoutes.AuthScreen.route) { inclusive = true }
            }
        }
        is AuthResult.Unauthorized<*> -> {
            Toast.makeText(
                context,
                result.message, // Usa il messaggio dalla classe `AuthResult`
                Toast.LENGTH_LONG
            ).show()
        }
        is AuthResult.UnknownError<*> -> {
            Toast.makeText(
                context,
                result.message ?: "An unknown error occurred", // Usa il messaggio dalla classe o un fallback
                Toast.LENGTH_LONG
            ).show()
        }
    }
}






//@Composable
//fun AuthScreen(
//    viewModel : MainViewModel = viewModel()
//) {
//    val navigator = NavigationAuth()
//
//
//    val state = viewModel.state
//    val context = LocalContext.current
//    LaunchedEffect(viewModel, context) {
//        viewModel.authResults.collect { result ->
//            when(result) {
//                is AuthResult.Authorized -> {
//                    navigator.navigate(SecretScreenDestination) {
//                        popUpTo(AuthScreenDestination.route) {
//                            inclusive = true
//                        }
//                    }
//                }
//                is AuthResult.Unauthorized -> {
//                    Toast.makeText(
//                        context,
//                        "You're not authorized",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//                is AuthResult.UnknownError -> {
//                    Toast.makeText(
//                        context,
//                        "An unknown error occurred",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        }
//    }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(32.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        TextField(
//            value = state.signUpUsername,
//            onValueChange = {
//                viewModel.onEvent(AuthUiEvent.SignUpUsernameChanged(it))
//            },
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = {
//                Text(text = "Username")
//            }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        TextField(
//            value = state.signUpPassword,
//            onValueChange = {
//                viewModel.onEvent(AuthUiEvent.SignUpPasswordChanged(it))
//            },
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = {
//                Text(text = "Password")
//            }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                viewModel.onEvent(AuthUiEvent.SignUp)
//            },
//            modifier = Modifier.align(Alignment.End)
//        ) {
//            Text(text = "Sign up")
//        }
//
//        Spacer(modifier = Modifier.height(64.dp))
//
//        TextField(
//            value = state.signInUsername,
//            onValueChange = {
//                viewModel.onEvent(AuthUiEvent.SignInUsernameChanged(it))
//            },
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = {
//                Text(text = "Username")
//            }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        TextField(
//            value = state.signInPassword,
//            onValueChange = {
//                viewModel.onEvent(AuthUiEvent.SignInPasswordChanged(it))
//            },
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = {
//                Text(text = "Password")
//            }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                viewModel.onEvent(AuthUiEvent.SignIn)
//            },
//            modifier = Modifier.align(Alignment.End)
//        ) {
//            Text(text = "Sign in")
//        }
//    }
//    if (state.isLoading) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    }
//}
