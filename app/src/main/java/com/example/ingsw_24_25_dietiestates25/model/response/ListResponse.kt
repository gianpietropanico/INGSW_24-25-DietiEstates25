package com.example.ingsw_24_25_dietiestates25.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ListResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
)