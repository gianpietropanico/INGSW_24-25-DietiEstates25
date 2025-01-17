package com.example.ingsw_24_25_dietiestates25.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu

@Composable
fun LinkText(
    linkText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier, // Aggiunto il parametro per la posizione
    fontSize: TextUnit = 20.sp // Aggiunto il parametro per la dimensione del testo
) {
    val context = LocalContext.current // Conserva il contesto per eventuali utilizzi

    Text(
        text = linkText, // Testo del link
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = fontSize, // Utilizza la dimensione del testo passata come parametro
            textDecoration = TextDecoration.Underline // Sottolineatura per il link
        ),
        textAlign = TextAlign.Center, // Allineamento centrale
        color = primaryBlu, // Colore del testo
        modifier = modifier
            .fillMaxWidth() // Larghezza massima
            .clickable {
                // Esempio di utilizzo del context: mostra un Toast o naviga
                Toast.makeText(context, "Link cliccato!", Toast.LENGTH_SHORT).show()
                onClick()
                // {TODO NAVIGATE TROUGH THE JUNGLE in the forgot password }
            }
    )
}


