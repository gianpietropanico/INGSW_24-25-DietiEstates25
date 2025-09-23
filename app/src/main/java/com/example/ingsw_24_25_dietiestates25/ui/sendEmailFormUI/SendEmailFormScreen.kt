package com.example.ingsw_24_25_dietiestates25.ui.sendEmailFormUI

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.SysAdminViewModel
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import com.example.ingsw_24_25_dietiestates25.ui.utils.MinimalTextField
import com.example.ingsw_24_25_dietiestates25.ui.utils.drawableToBase64

@Composable
fun SendEmailFormScreen(
    navController: NavController,
    mailerSenderVm: MailerSenderViewModel
) {
    val context = LocalContext.current
    val state by mailerSenderVm.state.collectAsState()
    var username by remember { mutableStateOf("") }
    var recipientEmail by remember { mutableStateOf("") }
    var showResultMessage by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.rectangleblu),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .offset(y = (-40).dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // topbar con icone agli estremi
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = bluPerchEcipiace,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Conferma",
                    tint = bluPerchEcipiace,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            showResultMessage = false
                            val profilePicBase64 = drawableToBase64(context, R.drawable.account_circle_blue)

                            mailerSenderVm.addUserBySendingEmail(
                                recipientEmail,
                                username,
                                profilePicBase64!!
                            )

                            if (state.resultMessage != null) {
                                showResultMessage = true
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // titolo
            Text(
                text = "Create An Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = bluPerchEcipiace,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(42.dp))

            // descrizione e messaggi
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "In these fields you can add a new user. Enter their email address to send login credentials along with a default username for setting up the account",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                if (showResultMessage) {
                    Text(
                        text = state.resultMessage ?: "",
                        color = if (state.success) Color.Green else DarkRed,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )
                }

                Spacer(Modifier.height(46.dp))
            }

            Spacer(modifier = Modifier.height(54.dp))

            // campo email
            MinimalTextField(
                value = recipientEmail,
                onValueChange = { recipientEmail = it },
                label = "Email",
                leadingIcon = null,
                placeholder = "Provide email so we can send credentials",
                modifier = Modifier.width(320.dp),
                onError = false
            )

            Spacer(modifier = Modifier.height(42.dp))

            // campo username
            MinimalTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                leadingIcon = null,
                placeholder = "Insert username",
                modifier = Modifier.width(320.dp),
                onError = false
            )
        }

        // overlay caricamento
        LoadingOverlay(isVisible = state.isLoading)
    }
}
