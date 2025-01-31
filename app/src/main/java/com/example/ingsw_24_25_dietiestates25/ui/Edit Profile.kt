package com.example.ingsw_24_25_dietiestates25.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import com.example.ingsw_24_25_dietiestates25.utils.loadProfileImage
import com.example.ingsw_24_25_dietiestates25.utils.rememberImagePicker
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import kotlinx.coroutines.launch


@OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun EditProfileScreen(onBackPress: () -> Unit = {}) {

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    val scope = rememberCoroutineScope()


    // Ottieni la funzione per aprire la galleria
    val openGallery = rememberImagePicker { uri -> profileImageUri = uri }

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

            // Header con freccia indietro e pulsante di modifica con animazione
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (isEditing) {
                            saveUserData(name, surname, email, username)
                        }
                        onBackPress()
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

                // **Animazione tra matita e "Done"**
                AnimatedContent(
                    targetState = isEditing,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
                    },
                    label = "Edit to Done Animation"
                ) { editing ->
                    if (editing) {
                        Text(
                            text = "Done",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = primaryBlu,
                            modifier = Modifier
                                .clickable {
                                    isEditing = false
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Modifiche salvate", duration = SnackbarDuration.Short)
                                    }
                                }
                                .padding(8.dp)
                        )
                    } else {
                        IconButton(
                            onClick = { isEditing = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Modifica",
                                tint = primaryBlu,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Immagine del profilo con icona sovrapposta
            Box(
                modifier = Modifier
                    .size(170.dp) // Dimensione dell'immagine del profilo
                    .align(Alignment.CenterHorizontally) // Centra la Box nella Column
            ) {
                // Mostra l'immagine selezionata o l'immagine di default
                Image(
                    painter = loadProfileImage(profileImageUri, R.drawable.adolfo),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent),
                    contentScale = ContentScale.Crop
                )


                Image(
                    painter = painterResource(id = R.drawable.changephoto),
                    contentDescription = "Change Photo",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 8.dp, y = 8.dp)
                        .clickable { openGallery() } // Apre la galleria
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Campi dell'utente
            FieldName(label = "Name", value = name, isEditing = isEditing, onValueChange = { name = it })

            FieldName(label = "Surname", value = surname, isEditing = isEditing, onValueChange = { surname = it })

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