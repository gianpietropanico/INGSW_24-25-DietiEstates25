package com.example.ingsw_24_25_dietiestates25.data.auth

import com.example.ingsw_24_25_dietiestates25.data.model.UserPayload

@kotlinx.serialization.Serializable
data class AuthResponse(
    val jwt: String, // Il token JWT ricevuto dal server
    val user: UserPayload // Il payload dell'utente associato al token
)
