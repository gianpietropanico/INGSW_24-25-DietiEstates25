package com.example.ingsw_24_25_dietiestates25.ui.authUI

import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.ui.authUI.socialbutton.GitHubCallbackManager
import com.example.ingsw_24_25_dietiestates25.ui.authUI.socialbutton.SocialLoginSection
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlueWithOpacity
import com.example.ingsw_24_25_dietiestates25.ui.utils.GradientButton
import com.example.ingsw_24_25_dietiestates25.ui.utils.GradientButtonWhite
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay


@Composable
fun WelcomeScreen(
    am : AuthViewModel,
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

//    state.localError = true
//    state.resultMessage = "VABBè FORZA NAPOLI SKIBIDIPOOPPY"

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated == true) {
            am.clearState()
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

            Spacer(modifier = Modifier.height(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Custom icon",
                tint = Color.Unspecified,
                modifier = Modifier.size(210.dp)
            )
            Spacer(modifier = Modifier.height(21.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "Welcome to DietiEstates25!",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                )
                Text(
                    text = "Browse thousands of listings, find your dream home, and make it yours with a single click.",
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(21.dp))

                Row {
                    Text(
                        text = "If you have an Agency ",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    )

                    Text(
                        text = "send a Request!",
                        fontSize = 14.sp,
                        lineHeight =  20.sp,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 14.sp,
                            color = primaryBlueWithOpacity.copy(alpha = 0.95f),
                        ),
                        modifier = Modifier
                            .clickable {
                                navController.navigate(NavigationItem.AgencySignIn.route)
                                am.clearResultMessage()
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(28.dp))

            GradientButton(
                text = "Sign In",
                onClick = {
                    navController.navigate(NavigationItem.SignIn.route)
                    am.clearResultMessage()
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            GradientButtonWhite(
                text = "Sign Up",
                onClick = {
                    navController.navigate(NavigationItem.SignUp.route)
                    am.clearResultMessage()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            //commentare gli argomenti del click per la preview
            SocialLoginSection(
                isLoading = am.isLoading(),
                onGoogleClick = {
                    am.startGoogleLogin(context)
                },
                onFacebookClick = {
                    am.startFacebookLogin(activity)
                },
                onGithubClick = {
                    am.startGithubLogin(context)
                },
                iconSize = 60.dp,
                spacing = 45.dp
            )

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
        }
        //Funzione per mettere a schermo il caricamento
        LoadingOverlay(isVisible = state.isLoading)

    }
}





