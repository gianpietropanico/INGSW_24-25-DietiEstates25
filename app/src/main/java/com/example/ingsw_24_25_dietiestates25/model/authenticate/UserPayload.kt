package com.example.ingsw_24_25_dietiestates25.model.authenticate

@kotlinx.serialization.Serializable
data class UserPayload(
    val id: String, // Identificativo unico dell'utente
    val email: String, // Email dell'utente
    val username: String, // Nome utente
    val role: String? = null // Ruolo dell'utente (opzionale)
)