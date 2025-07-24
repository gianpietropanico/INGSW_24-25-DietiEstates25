package com.example.ingsw_24_25_dietiestates25.ui.authUI

import androidx.activity.ComponentActivity
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.repository.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.result.AuthResult
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.ui.authUI.socialbutton.GitHubCallbackManager
import com.example.ingsw_24_25_dietiestates25.ui.authUI.socialbutton.SocialLoginSection
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlueWithOpacity
import com.example.ingsw_24_25_dietiestates25.ui.utils.GradientButton
import com.example.ingsw_24_25_dietiestates25.ui.utils.MinimalPasswordField
import com.example.ingsw_24_25_dietiestates25.ui.utils.MinimalTextField

@Composable
fun  SignUpScreen (
    am: AuthViewModel,
    navController: NavController
) {
    //commentare da qui fino a context per vedere la preview
    LaunchedEffect(Unit) {
        GitHubCallbackManager.register { code, state ->
            am.onOAuthResponse(code, state)
        }
    }
    val activity = LocalContext.current as ComponentActivity
    val context = LocalContext.current

    val state by am.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

//      stati per monitorare la preview
//    state.localError = true
//    state.resultMessage = "VABBè FORZA NAPOLI SKIBIDIPOOPPY"

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated == true) {
            navController.navigate(NavigationItem.Home.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Icona personalizzata",
                tint = Color.Unspecified,
                modifier = Modifier.size(210.dp)
            )

            MinimalTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = ImageVector.vectorResource(id = R.drawable.mail),
                placeholder = "Inserisci la tua email",
                modifier = Modifier.width(320.dp),
                onError = state.localError
            )


            Spacer(modifier = Modifier.height(8.dp))

            MinimalPasswordField(
                "password",
                password = password,
                onPasswordChange = {
                    password = it
                },
                passwordVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible },
                onError = state.localError,
                modifier = Modifier.width(320.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            MinimalPasswordField(
                "password again",
                password = confirmPassword,
                onPasswordChange = {
                    confirmPassword = it
                },
                passwordVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible },
                onError = state.localError,
                modifier = Modifier.width(320.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (state.resultMessage != null) {
                Text(
                    text = state.resultMessage ?: "",
                    color = if (state.success) Color.Green else DarkRed,
                    style = MaterialTheme.typography.labelLarge.copy( fontSize = 14.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            GradientButton(
                text = "Sign Up",
                onClick = {
                    state.confirmPassword = confirmPassword
                    am.signUpUser(email, password)
                }
            )

            //commentare gli argomenti del click per la preview
            SocialLoginSection(
                isLoading = am.isLoading(),
                onGoogleClick = { am.startGoogleLogin(context) },
                onFacebookClick = { am.startFacebookLogin(activity) },
                onGithubClick = {  am.startGithubLogin(context)  },
                iconSize = 60.dp, // personalizzabile
                spacing = 45.dp
            )

            Row {
                Text(
                    text = "Already have an account?    ",
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                )

                Text(
                    text = "Sign In!",
                    fontSize = 14.sp,
                    lineHeight =  20.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 14.sp,
                        color = primaryBlueWithOpacity.copy(alpha = 0.95f),
                    ),
                    modifier = Modifier
                        .clickable {
                            navController.navigate(NavigationItem.SignIn.route)
                        }
                )
            }

        }

        //Funzione per mettere a schermo il caricamento
        LoadingOverlay(isVisible = state.isLoading)

    }
}

@Preview(showBackground = true, name = "Sign Up Screen Preview")
@Composable
fun SignUpScreenPreview() {
    val navController = rememberNavController()

    val fakeViewModel = AuthViewModel(
        authRepository = FakeAuthRepository(),
        userSessionManager = UserSessionManager()
    )

    SignUpScreen(
        am = fakeViewModel,
        navController = navController
    )
}

class FakeAuthRepository : AuthRepository {

    override suspend fun signUp(email: String, password: String): AuthResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(email: String, password: String): AuthResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): AuthResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun authWithThirdParty(email: String, username: String): AuthResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun sendAgencyRequest(
        email: String,
        password: String,
        agencyName: String
    ): AuthResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchState(): AuthResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun exchangeGitHubCode(code: String?, state: String?): AuthResult<Unit> {
        TODO("Not yet implemented")
    }
}