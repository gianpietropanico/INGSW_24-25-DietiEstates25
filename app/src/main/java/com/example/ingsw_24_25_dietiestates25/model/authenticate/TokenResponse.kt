package com.example.ingsw_24_25_dietiestates25.model.authenticate

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)
