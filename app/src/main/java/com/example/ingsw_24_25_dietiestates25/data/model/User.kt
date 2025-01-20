package com.example.ingsw_24_25_dietiestates25.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String, // O ObjectId, in base alla configurazione
    val username: String,
    val email: String,
    val type: String
)