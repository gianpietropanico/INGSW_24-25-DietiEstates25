package com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI
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

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import com.example.ingsw_24_25_dietiestates25.ui.utils.MinimalTextField
import com.example.ingsw_24_25_dietiestates25.ui.utils.drawableToBase64

@Composable
fun SysAdminFormSuppScreen(
    navController: NavController,
    sysAdminVm : SysAdminViewModel
) {
    val context = LocalContext.current
    val state by sysAdminVm.state.collectAsState()
    var username by remember { mutableStateOf("") }
    var recipientEmail by remember { mutableStateOf("") }
    var showErrorMessage by remember { mutableStateOf(false) }


    LaunchedEffect(state.success) {
        if (state.success) {
            navController.popBackStack()
        }
    }

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
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = bluPerchEcipiace,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            sysAdminVm.clearResultMessage()
                            navController.popBackStack()
                        }
                )

                Text(
                    text = "Insert Support Admin",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = bluPerchEcipiace
                )

                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Conferma",
                    tint = bluPerchEcipiace,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            sysAdminVm.clearResultMessage()
                            showErrorMessage = false

                            val profilePicBase64 = drawableToBase64(context, R.drawable.account_circle_blue)
                            sysAdminVm.addSuppAdmin(recipientEmail, username, profilePicBase64!!)

                            if (!state.success && state.resultMessage != null) {
                                showErrorMessage = true
                            }

                        }
                )
            }
            Text(
                text = "In these fields you can add a support admin. Enter the admin’s email address to send them the login credentials along with a default username for setting up their account." ,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(150.dp))

            MinimalTextField(
                value = recipientEmail,
                onValueChange = { recipientEmail = it },
                label = "Email",
                leadingIcon = null,
                placeholder = "Provide email so we can send credentials",
                modifier = Modifier.width(320.dp),
                onError = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            MinimalTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                leadingIcon = null,
                placeholder = "Insert your username",
                modifier = Modifier.width(320.dp),
                onError = false
            )

            if (showErrorMessage) {

                androidx.compose.material3.Text(
                    text = state.resultMessage ?: "",
                    color = if (state.success) Color.Green else DarkRed,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )

            }

        }

        LoadingOverlay(isVisible = state.isLoading)
    }
}