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
    val agent: User?
)

@Serializable
enum class Type {
    @SerialName("Rent")
    RENT,

    @SerialName("Sell")
    SELL
}

fun PropertyListing.toLightCopy(): PropertyListing {
    return this.copy(
        title = this.title,
        id = this.id,
        agent = this.agent,
        type = null,      // oppure lascia originale se vuoi
        price = 0f,       // opzionale
        property = Property(
            city = this.property.city,
            street = this.property.street,
            civicNumber = this.property.civicNumber,
            cap = "",
            country = "",
            province = "",
            latitude = 0.0,
            longitude = 0.0,
            pois = emptyList(),
            images = emptyList(),
            numberOfRooms = 0,
            numberOfBathrooms = 0,
            size = 0f,
            energyClass = EnergyClass.A,
            parking = false,
            garden = false,
            elevator = false,
            gatehouse = false,
            balcony = false,
            roof = false,
            airConditioning = false,
            heatingSystem = false,
            description = ""
        )
    )
}