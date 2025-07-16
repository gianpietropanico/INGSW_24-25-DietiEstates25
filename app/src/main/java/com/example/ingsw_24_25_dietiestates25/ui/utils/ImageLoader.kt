package com.example.ingsw_24_25_dietiestates25.ui.utils


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter

@Composable
fun loadProfileImage(imageUri: Uri?, defaultImageRes: Int): Painter {
    return if (imageUri != null) {
        rememberAsyncImagePainter(imageUri) // Carica immagine dalla galleria
    } else {
        painterResource(id = defaultImageRes) // Carica immagine da drawable
    }
}
