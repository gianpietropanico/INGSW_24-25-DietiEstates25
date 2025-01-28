package com.example.ingsw_24_25_dietiestates25.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu

@Composable
fun ChangePasswordScreen() {

//    var currentPassword by remember { mutableStateOf("") }
//    var newPassword by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Sfondo bianco per l'intero schermo
    ) {
        // Immagine di sfondo
        Image(
            painter = painterResource(id = R.drawable.rectangleblu),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Altezza personalizzata dell'immagine di sfondo
                .align(Alignment.TopCenter), // Posizionata in alto al centro
            contentScale = ContentScale.Crop // Ritaglia l'immagine per adattarla alla larghezza
        )

        // Contenuto principale
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp) // Padding orizzontale per il contenuto
        ) {
            // Spacer per spostare gli elementi più in basso
            Spacer(modifier = Modifier.height(50.dp)) // Altezza regolabile

            // Contenitore per l'icona e il testo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), // Padding aggiuntivo dall'alto
                verticalAlignment = Alignment.CenterVertically // Allinea verticalmente l'icona e il testo
            ) {
                // Icona frecciaback
                Icon(
                    painter = painterResource(id = R.drawable.frecciaback),
                    contentDescription = "Back Icon",
                    tint = primaryBlu, // Colore dell'icona
                    modifier = Modifier.size(34.dp) // Dimensione dell'icona
                )

                // Spazio tra icona e testo
                Spacer(modifier = Modifier.width(26.dp))

                // Testo Change Password
                Text(
                    text = "Change password",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 34.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(50.dp)) // spazio tra change pass e pass

            // Campo 1: Current Password
            PasswordField(
                label = "Current Password",
                placeholder = "Enter your current password"
            )

            // Campo 2: New Password
            PasswordField(
                label = "New Password",
                placeholder = "Enter your new password"
            )

            // Campo 3: Confirm Password
            PasswordField(
                label = "Confirm Password",
                placeholder = "Re-enter your new password"
            )

        }
    }
}




@Composable
fun PasswordField(
    label: String, // Testo sopra la TextField
    placeholder: String // Testo placeholder dentro la TextField



) {

  var  primaryBlueWithOpacity: Color = primaryBlu.copy(alpha = 0.1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Padding laterale per il contenitore
    ) {
        // Testo sopra la TextField
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp) // Spazio tra il testo e la TextField
        )

        // TextField personalizzata
        OutlinedTextField(
            value = "", // Valore vuoto (solo grafica)
            onValueChange = {}, // Nessuna logica per ora
            placeholder = {
                Text(
                    text = placeholder, // Placeholder dinamico
                    color = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp), // Altezza della TextField
            shape = RoundedCornerShape(8.dp), // Bordo arrotondato
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = primaryBlueWithOpacity, // Colore interno: primaryblue
                focusedBorderColor = Color(0xFF5B6067), // Colore bordo quando in focus
                unfocusedBorderColor = Color(0xFF5B6067), // Colore bordo quando non in focus
                textColor = Color.Black // Colore del testo
            ),
            visualTransformation = PasswordVisualTransformation(), // Nasconde il testo inserito
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password // Tipo di tastiera per password
            )
        )
    }
}




@Preview(showBackground = true)
@Composable
fun ChangePasswordPreview() {
    ChangePasswordScreen()
}