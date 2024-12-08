package com.example.ingsw_24_25_dietiestates25.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)
