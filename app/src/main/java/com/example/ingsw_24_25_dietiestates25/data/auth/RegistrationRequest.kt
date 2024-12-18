package com.example.ingsw_24_25_dietiestates25.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val agencyName: String,
    val email: String
)
