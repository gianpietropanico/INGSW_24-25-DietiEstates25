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
import com.example.ingsw_24_25_dietiestates25.ui.viewmodels.AuthViewModel


@Composable
fun HomeApp(
    viewModel: AuthViewModel,
    onLogoutClicked: () -> Unit
) {
    // Stato per verificare se l'utente è loggato
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoggedIn) {
            Text(text = "Benvenuto! Sei loggato.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.logout() // Azione per fare il logout
                onLogoutClicked()  // Callback per tornare alla schermata di login
            }) {
                Text("Logout")
            }
        } else {
            Text(text = "Non sei loggato. Torna alla schermata di login.")
        }
    }
}

