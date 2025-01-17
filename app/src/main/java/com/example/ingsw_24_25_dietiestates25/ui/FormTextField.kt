package com.example.ingsw_24_25_dietiestates25.ui



import android.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

//STATELESS

@Composable
fun FormTextField(
    value: String, // Il valore attuale del campo
    onValueChange: (String) -> Unit, // Callback per gestire l'input
    label: String, // Etichetta del campo
    placeholder: String, // Testo segnaposto
    keyboardType: KeyboardType, // Tipo di tastiera
    isPassword: Boolean = false // Se il campo è per password
) {

    OutlinedTextField(
        value = value, // Usa il valore passato
        onValueChange = onValueChange, // Usa la callback passata
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        shape = RoundedCornerShape(16.dp) // Aggiunge bordi arrotondati con un raggio di 16.dp
    )
}


