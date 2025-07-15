package com.example.ingsw_24_25_dietiestates25.ui.authenticate

import android.content.Context

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.R
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.ingsw_24_25_dietiestates25.ui.components.social.FacebookLoginButton
import com.example.ingsw_24_25_dietiestates25.ui.components.social.GitHubButton
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.draw.clip

import com.example.ingsw_24_25_dietiestates25.ui.components.social.GoogleSignInButton
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem

@Composable
fun AuthScreen(
    am: AuthViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val state = am.authState.collectAsState().value
    var isLogin by remember { mutableStateOf(true) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var showResetPassword by remember { mutableStateOf(false) }

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated == true) {
            navController.navigate(NavigationItem.Home.route)

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

            when {
                showResetPassword -> {
                    // Schermata Reset Password
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Reset your password",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", style = MaterialTheme.typography.titleMedium) },
                        placeholder = {
                            Text(
                                "Enter your email to reset",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PasswordField(
                        "Old password",
                        password = password,
                        onPasswordChange = {
                            password = it
                            am.clearErrorMessage()
                        },
                        passwordVisible = passwordVisible,
                        onVisibilityToggle = { passwordVisible = !passwordVisible },
                        isError = state.errorMessage != null
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PasswordField(
                        "New password",
                        password = newPassword,
                        onPasswordChange = {
                            newPassword = it
                            am.clearErrorMessage()
                        },
                        passwordVisible = passwordVisible,
                        onVisibilityToggle = { passwordVisible = !passwordVisible },
                        isError = state.errorMessage != null
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            am.sendResetPasswordEmail(email, password, newPassword)
                            showResetPassword = false // torna indietro dopo l’invio
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(
                            "Send Reset Link",
                            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onPrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Back to Login",
                        modifier = Modifier
                            .clickable {
                                showResetPassword = false
                            }
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.primary
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    if (state.errorMessage != null) {
                        Text(
                            text = state.errorMessage ?: "",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )
                    }
                }

                else -> {

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", style = MaterialTheme.typography.titleMedium) },
                        placeholder = {
                            Text(
                                "Enter your Email",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)), // <-- Applica corner visivo
                        shape = RoundedCornerShape(16.dp), // <-- Applica alla struttura del field
                    )


                    Spacer(modifier = Modifier.height(8.dp))


                    PasswordField(
                        "Password",
                        password = password,
                        onPasswordChange = {
                            password = it
                            am.clearErrorMessage()
                        },
                        passwordVisible = passwordVisible,
                        onVisibilityToggle = { passwordVisible = !passwordVisible },
                        isError = state.errorMessage != null
                    )


                    if (!isLogin) {
                        Spacer(modifier = Modifier.height(8.dp))
                        PasswordField(
                            "Confirm Password",
                            password = confirmPassword,
                            onPasswordChange = {
                                confirmPassword = it
                                am.clearErrorMessage()
                            },
                            passwordVisible = passwordVisible,
                            onVisibilityToggle = { passwordVisible = !passwordVisible },
                            isError = state.errorMessage != null,
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Forgot password?",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showResetPassword = true
                            }
                            .padding(end = 4.dp),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (isLogin) {
                                am.signInUser(email, password)
                            } else {
                                if (password == confirmPassword) {
                                    am.signUpUser(email, password)
                                } else {
                                    am.clearErrorMessage()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(
                            text = if (isLogin) "LOG IN" else "SIGN UP",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    if (state.errorMessage != null) {
                        Text(
                            text = state.errorMessage ?: "",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    SocialLoginSection(
                        am = am,
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
    }
}


@Composable
fun PasswordField(
    passwordLabel: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    isError: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(passwordLabel, style = MaterialTheme.typography.titleMedium) },
            placeholder = { Text("Enter your $passwordLabel", style = MaterialTheme.typography.bodyMedium) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
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
    }

}

@Composable
private fun SocialLoginSection(
    am: AuthViewModel,
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

            GoogleSignInButton(context, am)
            FacebookLoginButton(am)
            GitHubButton(am)


        }
        Spacer(modifier = Modifier.width(16.dp))
    }
}

