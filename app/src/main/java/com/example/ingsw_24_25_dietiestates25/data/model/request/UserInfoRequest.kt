package com.example.ingsw_24_25_dietiestates25.data.model.request
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoRequest(
    val email: String,
    val value : String,
    val typeRequest: String
)
