package com.example.ingsw_24_25_dietiestates25.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ImageRequest(
    val ownerId : String,
    val base64Images : List<String> // max 2 immagini in base64
)