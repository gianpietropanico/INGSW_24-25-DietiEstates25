package com.example.ingsw_24_25_dietiestates25.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.ui.authUI.AuthViewModel
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem


@Composable
fun HomeScreen(
    am: AuthViewModel,
    navController: NavController
) {
    val currentUser by am.userSessionManager.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Benvenuto! Sei loggato:  ${currentUser?.username}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            am.logout()
            navController.navigate(NavigationItem.Auth.route)
        }) {
            Text("Logout")
        }
    }
}

