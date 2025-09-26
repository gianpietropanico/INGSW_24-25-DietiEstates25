package com.example.ingsw_24_25_dietiestates25.data.model.dataclass

import kotlinx.serialization.Serializable


@Serializable
data class POI(
    val name: String,
    val type: String,
    val lat: Double,
    val lon: Double,
    val distance: Double
)