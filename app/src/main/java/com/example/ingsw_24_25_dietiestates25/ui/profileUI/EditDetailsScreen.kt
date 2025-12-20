package com.example.ingsw_24_25_dietiestates25.ui.profileUI

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
import com.example.ingsw_24_25_dietiestates25.ui.utils.MinimalPasswordField
import com.example.ingsw_24_25_dietiestates25.ui.utils.MinimalTextField

@Composable
fun ProfileEditDetailsScreen(
    navController: NavController,
    pm: ProfileViewModel
) {

    val state by pm.state.collectAsState()
    var value by remember { mutableStateOf(pm.value) }

    var name by remember { mutableStateOf(( pm.getName() )) }
    var surname by remember { mutableStateOf(pm.getSurname()) }

    var password by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
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
                            pm.clearResultMessage()
                            navController.popBackStack()
                        }
                )

                Text(
                    text = pm.label,
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
                            pm.clearResultMessage()
                            showErrorMessage = false

                            when (pm.label) {
                                "Password" -> {
                                    pm.sendResetPasswordEmail(newPassword = newPassword, oldPassword = password)
                                }
                                "Name And Surname" -> {
                                    pm.updateUserInfo("$name $surname", pm.label)
                                }
                                else -> {
                                    pm.updateUserInfo(value, pm.label)
                                }
                            }

                            if (!state.success && state.resultMessage != null ){
                                showErrorMessage = true
                            }

                        }
                )
            }

            Spacer(modifier = Modifier.height(150.dp))

            if (pm.label == "Password") {

                Spacer(modifier = Modifier.height(8.dp))

                MinimalPasswordField(
                    "Old password",
                    password = password,
                    onPasswordChange = {
                        password = it
                    },
                    passwordVisible = passwordVisible,
                    onVisibilityToggle = { passwordVisible = !passwordVisible },
                    onError = false,
                    modifier = Modifier.width(320.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                MinimalPasswordField(
                    "New password",
                    password = newPassword,
                    onPasswordChange = {
                        newPassword = it
                    },
                    passwordVisible = passwordVisible,
                    onVisibilityToggle = { passwordVisible = !passwordVisible },
                    onError = false,
                    modifier = Modifier.width(320.dp)
                )

            } else if (pm.label == "Name And Surname"){


                MinimalTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Name",
                    leadingIcon = null,
                    placeholder = "Insert your name",
                    modifier = Modifier.width(320.dp),
                    onError = false
                )

                Spacer(modifier = Modifier.height(24.dp))

                MinimalTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = "Surname",
                    leadingIcon = null,
                    placeholder = "Insert your surname",
                    modifier = Modifier.width(320.dp),
                    onError = false
                )

            }else{
                MinimalTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = pm.label,
                    leadingIcon = null,
                    placeholder = "Insert your username",
                    modifier = Modifier.width(320.dp),
                    onError = false
                )
            }

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

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = pm.description ,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            if( pm.getUserType() == "THIRDPARTY_USER" && pm.label == "Password"){

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "YOUR DEFAULT PASSWORD IS : ${pm.getUserId()}",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

        }

        LoadingOverlay(isVisible = state.isLoading)
    }
}
