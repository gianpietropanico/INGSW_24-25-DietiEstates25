package com.example.ingsw_24_25_dietiestates25.ui.utils

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun rememberImagePicker(onImageSelected: (Uri?) -> Unit): () -> Unit {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri) // Passa l'URI dell'immagine selezionata alla UI
    }

    return { imagePickerLauncher.launch("image/*") } // Lancia il selettore della galleria
}
