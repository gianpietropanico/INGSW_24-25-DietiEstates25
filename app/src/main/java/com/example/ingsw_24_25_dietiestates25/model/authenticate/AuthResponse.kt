package com.example.ingsw_24_25_dietiestates25.model.authenticate

@kotlinx.serialization.Serializable
data class AuthResponse(
    val jwt: String, // Il token JWT ricevuto dal server
    val user: UserPayload // Il payload dell'utente associato al token
)
