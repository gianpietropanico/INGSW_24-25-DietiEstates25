package com.example.ingsw_24_25_dietiestates25.data.model.request

@kotlinx.serialization.Serializable
data class AdminRequest(
    val adminEmail: String,
    val adminId: String,
    val suppAdminEmail: String,
    val email : String
)
