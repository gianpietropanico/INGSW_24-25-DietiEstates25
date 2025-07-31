package com.example.ingsw_24_25_dietiestates25.model.dataclass

data class Property(
    val id: String,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val indicators: List<String> = emptyList(),
    val agentEmail: String
)
