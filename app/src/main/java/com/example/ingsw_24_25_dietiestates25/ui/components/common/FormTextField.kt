package com.example.ingsw_24_25_dietiestates25.ui.components.common



import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField


import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu


//STATELESS
@Composable
fun FormTextField(
    value: String, // Il valore attuale del campo
    onValueChange: (String) -> Unit, // Callback per gestire l'input
    label: String, // Etichetta del campo
    placeholder: String, // Testo segnaposto
    keyboardType: KeyboardType, // Tipo di tastiera
    isPassword: Boolean = false, // Se il campo è per password
    labelColor: Color = Color.Black // Colore personalizzabile dell'etichetta
) {
    OutlinedTextField(
        value = value, // Usa il valore passato
        onValueChange = onValueChange, // Usa la callback passata
        label = {
            Text(
                text = label,
                fontSize = 18.sp,
                color = labelColor // Applica il colore personalizzato all'etichetta
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 18.sp
            )
        }, // Dimensione del placeholder
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp), // Dimensione del testo visibile
        shape = RoundedCornerShape(16.dp), // Aggiunge bordi arrotondati con un raggio di 16.dp
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent, // Sfondo quando il campo è selezionato
            unfocusedContainerColor = Color.Transparent, // Sfondo quando il campo non è selezionato
            focusedIndicatorColor = primaryBlu, // Colore del bordo quando è selezionato
            unfocusedIndicatorColor = Color.Black, // Colore del bordo quando non è selezionato
            focusedTextColor = Color.Black, // Colore del testo quando il campo è selezionato
            unfocusedTextColor = Color.Black // Colore del testo quando il campo non è selezionato
        )
    )
}




