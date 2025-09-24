package com.example.ingsw_24_25_dietiestates25.data.model.dataclass


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropertyListing(
    val id: String,
    val title: String,
    val type: Type?,
    val price: Float,
    val property: Property,
    val agentEmail: String
)

@Serializable
enum class Type {
    @SerialName("Rent")
    RENT,

    @SerialName("Sell")
    SELL
}