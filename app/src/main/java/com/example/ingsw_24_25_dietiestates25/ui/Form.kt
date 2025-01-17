package com.example.ingsw_24_25_dietiestates25.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu


@Composable
fun Form(
    email: String,
    password: String,
    confirmPassword: String = "", // Aggiunto parametro per la conferma password
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: ((String) -> Unit)? = null, // Parametro opzionale
    linkText: String,
    onClickText: () -> Unit,
    onClickButton: () -> Unit,
    textButton: String,
    isSignUpMode: Boolean // Rinominato da valueSignIn
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        //horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Campo Email
        FormTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email Address",
            placeholder = "Insert your email",
            keyboardType = KeyboardType.Email
        )

        // Campo Password
        PasswordFields(
            password = password,
            confirmPassword = confirmPassword,
            onPasswordChange = onPasswordChange,
            onConfirmPasswordChange = onConfirmPasswordChange ?: {},
            valueSignIn = isSignUpMode
        )

        Row(
            modifier = Modifier
                .fillMaxWidth() // Riempi tutta la larghezza
                .padding(horizontal = 16.dp) // Aggiungi margini laterali
        ) {
        // Link per azioni aggiuntive, allineato a sinistra
        LinkText(
            linkText = linkText,
            onClick = onClickText,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth() // Occupa l'intera larghezza
                .padding(start = 16.dp) // Aggiungi un margine a sinistra
                //.align(Alignment.CenterHorizontally)
            // horizontalAlignment = Alignment.CenterHorizontally
        )
    }

        // Pulsante principale
        MyButton(
            text = textButton,
            onClick = onClickButton
        )
    }
}

@Composable
fun PasswordFields(
    password: String,
    confirmPassword: String,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    valueSignIn: Boolean,
    errorMessage: String? = null
) {
    // Primo campo password
    FormTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = "Password",
        placeholder = "Insert your password",
        keyboardType = KeyboardType.Password,
        isPassword = true
    )

    if (valueSignIn) {

        // Secondo campo password (conferma)
        FormTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Conferma Password",
            placeholder = "Insert again your password",
            keyboardType = KeyboardType.Password,
            isPassword = true
        )

        // Mostra il messaggio di errore
        if (errorMessage != null) {

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
fun MyButton(
    text: String,
    onClick: () -> Unit
) {


    Button(
        onClick = onClick, // Callback per il click
        modifier = Modifier
            .fillMaxWidth() // Larghezza massima
            .height(60.dp), // Altezza del pulsante

        colors = ButtonDefaults.buttonColors(
            containerColor = primaryBlu
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text, // Testo visualizzato sul pulsante
            fontSize = 25.sp, // Dimensione del testo
            style = MaterialTheme.typography.labelLarge // Stile del testo
        )
    }

}
