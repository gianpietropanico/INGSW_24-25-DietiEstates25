package com.example.ingsw_24_25_dietiestates25.ui.authenticate
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.R
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.ingsw_24_25_dietiestates25.ui.components.social.FacebookLoginButton
import com.example.ingsw_24_25_dietiestates25.ui.components.social.GitHubButton
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import com.example.ingsw_24_25_dietiestates25.ui.components.social.GoogleSignInButton
@Composable
fun AuthScreen(
    am: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val state = am.authState.collectAsState().value
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        am.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally // Allinea i contenuti al centro
            ) {

                Spacer(modifier = Modifier.height(50.dp))

                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Icona personalizzata",
                    tint = Color.Unspecified

                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", style = MaterialTheme.typography.labelMedium) },
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))


            PasswordField(
                password = state.signInPassword,
                onPasswordChange = { password = it },
                passwordVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible },
                isError = state.errorMessage != null,
                errorMessage = state.errorMessage
            )


            if (!isLogin) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password", style = MaterialTheme.typography.labelMedium) },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isLogin) {
                        am.signInUser(email, password)
                    } else {
                        if (password == confirmPassword) {
                            am.signUpUser(email, password)
                        } else {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isLogin) "LOG IN" else "SIGN UP",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            SocialLoginSection(
                navController = navController,
                authViewModel = am,
                context = context
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (isLogin) "Not a member? Register now!" else "Already a member? Login",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isLogin = !isLogin }
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center
            )
        }
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

@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    isError: Boolean,
    errorMessage: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password", style = MaterialTheme.typography.titleMedium) },
            placeholder = { Text("Enter your password", style = MaterialTheme.typography.bodyMedium) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = isError,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Default.Visibility
                else Icons.Default.VisibilityOff

                IconButton(onClick = onVisibilityToggle) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = if (isError) Color.Red else MaterialTheme.colorScheme.outline
            )
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.labelSmall, // oppure bodySmall se preferisci
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }

}
