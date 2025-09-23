package com.example.ingsw_24_25_dietiestates25.model.request

@kotlinx.serialization.Serializable
data class AdminRequest(
    val adminEmail: String,
    val adminId: String,
    val suppAdminEmail: String,
    val usernameSuppAdmin: String,
    val emailDomain: String
)
