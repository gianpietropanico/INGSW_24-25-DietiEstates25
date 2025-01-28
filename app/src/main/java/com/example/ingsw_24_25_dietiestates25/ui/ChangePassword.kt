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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu


@Composable
fun ChangePasswordScreen() {
    // Stati per le password
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
            Spacer(modifier = Modifier.height(50.dp)) // Spazio per spostare gli elementi in basso

            // Contenitore per l'icona e il testo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icona frecciaback
                Icon(
                    painter = painterResource(id = R.drawable.frecciaback),
                    contentDescription = "Back Icon",
                    tint = primaryBlu, // Colore dell'icona
                    modifier = Modifier.size(34.dp)
                )

                Spacer(modifier = Modifier.width(26.dp))

                // Testo "Change Password"
                Text(
                    text = "Change password",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 34.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(130.dp)) // Spazio tra il titolo e i campi password

            // Campo 1: Current Password
            PasswordField(
                label = "Current Password",
                placeholder = "Enter your current password",
                value = currentPassword,
                onValueChange = { currentPassword = it }
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Campo 2: New Password
            PasswordField(
                label = "New Password",
                placeholder = "Enter your new password",
                value = newPassword,
                onValueChange = { newPassword = it }
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Campo 3: Confirm Password
            PasswordField(
                label = "Confirm Password",
                placeholder = "Re-enter your new password",
                value = confirmPassword,
                onValueChange = { confirmPassword = it }
            )

            Spacer(modifier = Modifier.height(160.dp)) // Spazio tra i campi e il pulsante

            // Pulsante Save Changes
            Button(
                onClick = {
                    // Controlli sulle password
                    when {
                        currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() -> {
                            println("Errore: Tutti i campi devono essere compilati.")
                        }
                        currentPassword != "password_salvata_db" -> {
                            println("Errore: La password corrente non è corretta.")
                        }
                        newPassword != confirmPassword -> {
                            println("Errore: Le nuove password non corrispondono.")
                        }
                        newPassword.length < 8 -> {
                            println("Errore: La nuova password deve essere lunga almeno 8 caratteri.")
                        }
                        else -> {
                            println("Successo: La password è stata cambiata con successo.")
                            // TODO: Effettua la chiamata al server per salvare la nuova password
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = primaryBlu,
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "Save changes",
                    fontWeight = FontWeight.Normal,
                    fontSize = 30.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun PasswordField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    // Stato per controllare se la password è visibile
    var passwordVisible by remember { mutableStateOf(false) }

    val primaryBlueWithOpacity: Color = primaryBlu.copy(alpha = 0.1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Testo sopra la TextField
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // TextField personalizzata
        OutlinedTextField(
            value = value, // Stato dinamico
            onValueChange = onValueChange, // Aggiorna il valore della password
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray,
                    fontSize = 20.sp // Dimensione del placeholder
                )
            },
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp // Stessa dimensione del placeholder
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = primaryBlueWithOpacity,
                focusedBorderColor = Color(0xFF5B6067),
                unfocusedBorderColor = Color(0xFF5B6067),
                textColor = Color.Black
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                // Icona per alternare la visibilità della password
                val icon = if (passwordVisible) R.drawable.eyeoff else R.drawable.eyeon
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = description
                    )
                }
            }
        )
    }
}










@Preview(showBackground = true)
@Composable
fun ChangePasswordPreview() {
    ChangePasswordScreen()
}