package com.example.ingsw_24_25_dietiestates25.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ImageRequest(
    val ownerEmail: String? = null,
    val ownerId : String? = null,
    val base64Images : List<String> // max 2 immagini in base64
)