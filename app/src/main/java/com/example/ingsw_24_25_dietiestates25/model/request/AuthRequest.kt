package com.example.ingsw_24_25_dietiestates25.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String? = null,
    val email: String,
    val password: String? = null,
    val newPassword: String? = null,
    val agencyName: String? = null,
    val provider: String? = null
)