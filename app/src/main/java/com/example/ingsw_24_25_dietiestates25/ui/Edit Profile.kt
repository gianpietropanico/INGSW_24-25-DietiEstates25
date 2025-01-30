package com.example.ingsw_24_25_dietiestates25.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch


@Composable
fun EditProfileScreen(onBackPress: () -> Unit = {}) {

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Immagine di sfondo
        Image(
            painter = painterResource(id = R.drawable.rectangleblu),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Header con freccia e icona matita/spunta
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (isEditing) {
                            saveUserData(name, surname, email, username) // Chiamata alla funzione di salvataggio
                        }
                        onBackPress() // Torna indietro
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.frecciaback),
                        contentDescription = "Back Icon",
                        tint = primaryBlu,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        isEditing = !isEditing
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = if (isEditing) "Modalità modifica attivata" else "Modifiche salvate",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = "Modifica",
                        tint = primaryBlu,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Immagine del profilo con icona sovrapposta
            Box(
                modifier = Modifier
                    .size(170.dp) // Dimensione dell'immagine del profilo
                    .align(Alignment.CenterHorizontally) // Centra la Box nella Column
            ) {
                // Immagine del profilo
                Image(
                    painter = painterResource(id = R.drawable.adolfo),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape) // Ritaglia l'immagine in forma circolare
                        .background(Color.Transparent), // Sfondo trasparente
                    contentScale = ContentScale.Crop
                )

                // Icona "changephoto" sovrapposta in basso a destra e cliccabile
                Image(
                    painter = painterResource(id = R.drawable.changephoto),
                    contentDescription = "Change Photo",
                    modifier = Modifier
                        .size(60.dp) // Dimensione dell'icona
                        .align(Alignment.BottomEnd) // Posizionata in basso a destra
                        .offset(x = 20.dp, y = 10.dp) // Leggero spostamento per migliore visibilità
                        .clickable {
                            // TODO: Implementare la logica per cambiare la foto del profilo
                            println("Cambio foto premuto")
                        }
                )

            }

            Spacer(modifier = Modifier.height(30.dp))

            // Campi dell'utente
            FieldName(label = "Nome", value = name, isEditing = isEditing, onValueChange = { name = it })

            FieldName(label = "Cognome", value = surname, isEditing = isEditing, onValueChange = { surname = it })

            FieldName(label = "Email", value = email, isEditing = isEditing, onValueChange = { email = it })

            FieldName(label = "Username", value = username, isEditing = isEditing, onValueChange = { username = it })
        }

        // Mostra il messaggio di conferma
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun FieldName(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    val primaryBlueWithOpacity: Color = primaryBlu.copy(alpha = 0.1f)
    val borderColor = if (isEditing) Color.Blue.copy(alpha = 0.6f) else Color.Gray.copy(alpha = 0.5f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(bottom = 4.dp)
                .padding(start = 6.dp) // Spostato leggermente a destra
        )

        OutlinedTextField(
            value = value,
            onValueChange = { if (isEditing) onValueChange(it) },
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (!isEditing) Modifier.pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                awaitPointerEvent() // Blocca eventi touch
                            }
                        }
                    } else Modifier
                ),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = primaryBlueWithOpacity,
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                textColor = Color.Black
            ),
            singleLine = true,
            readOnly = !isEditing
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}


// Funzione TODO per il salvataggio dei dati
fun saveUserData(name: String, surname: String, email: String, username: String) {
    // TODO: Implementare la logica di salvataggio dei dati (es. chiamata API, database locale, ecc.)
    println("Salvataggio dati: Nome=$name, Cognome=$surname, Email=$email, Username=$username")
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen()
}