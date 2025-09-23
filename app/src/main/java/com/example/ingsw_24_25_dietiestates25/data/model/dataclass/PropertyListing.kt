package com.example.ingsw_24_25_dietiestates25.data.model.dataclass


import kotlinx.serialization.Serializable

@Serializable
data class PropertyListing(
    val id: String,
    val title: String,
    val type: Type,
    val price: Float,
    val property: Property,
    val agentEmail: String
)

enum class Type(val label: String) {
    A("Rent"),
    B("Sell"),

}