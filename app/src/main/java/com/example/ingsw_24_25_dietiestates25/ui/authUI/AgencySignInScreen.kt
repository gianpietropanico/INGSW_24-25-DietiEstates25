package com.example.ingsw_24_25_dietiestates25.ui.authUI
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.testmock.FakeAuthRepository
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlueWithOpacity
import com.example.ingsw_24_25_dietiestates25.ui.utils.GradientButton
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import com.example.ingsw_24_25_dietiestates25.ui.utils.MinimalPasswordField
import com.example.ingsw_24_25_dietiestates25.ui.utils.MinimalTextField

@Composable
fun  AgencySignInScreen (
    am: AuthViewModel,
    navController: NavController
) {

    val state by am.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var agencyName by remember { mutableStateOf("") }

//    state.localError = true
//    state.resultMessage = "VABBè FORZA NAPOLI SKIBIDIPOOPPY"

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))
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
                "Password",
                password = password,
                onPasswordChange = {
                    password = it
                },
                passwordVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible },
                onError = state.localError,
                modifier = Modifier.width(320.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            MinimalPasswordField(
                "Confirm password",
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

            MinimalTextField(
                value = agencyName,
                onValueChange = { agencyName = it },
                label = "Agency Name",
                leadingIcon = ImageVector.vectorResource(id = R.drawable.home_icon),
                placeholder = "Inserisci il nome della tua agenzia",
                modifier = Modifier.width(320.dp),
                onError = state.localError
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (state.resultMessage != null) {

                Text(
                    text = state.resultMessage ?: "",
                    color = if (state.success) Color.Green else Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )

            }

            Spacer(modifier = Modifier.height(6.dp))

            GradientButton(
                text = "Send Request",
                onClick = {
                    state.confirmPassword = confirmPassword

                    am.sendAgencyRequest(agencyName, email, password)
                }
            )


            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Go Back",
                modifier = Modifier
                    .clickable {
                        am.clearResultMessage()
                        navController.navigate(NavigationItem.Welcome.route)
                    }
                    .padding(8.dp),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 14.sp,
                    color = primaryBlueWithOpacity.copy(alpha = 0.95f),
                ),
                textAlign = TextAlign.Center

            )

        }

        //Funzione per mettere a schermo il caricamento
        LoadingOverlay(isVisible = state.isLoading)

    }
}



