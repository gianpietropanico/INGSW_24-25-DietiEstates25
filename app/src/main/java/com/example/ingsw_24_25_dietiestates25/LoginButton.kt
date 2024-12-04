package com.example.ingsw_24_25_dietiestates25
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LoginButton(text: String, onClick: () -> Unit) {


    Button(
        onClick = onClick, // Callback per il click
        modifier = Modifier
            .fillMaxWidth() // Larghezza massima
            .height(48.dp), // Altezza del pulsante
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary, // Colore del contenitore
            contentColor = Color.White // Colore del testo
        )
    ) {
        Text(
            text = text, // Testo visualizzato sul pulsante
            fontSize = 16.sp, // Dimensione del testo
            style = MaterialTheme.typography.labelLarge // Stile del testo
        )
    }

}

