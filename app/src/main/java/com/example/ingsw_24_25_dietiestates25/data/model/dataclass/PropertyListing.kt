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
@Serializable
data class ListingSummary(
    val id: String,
    val title: String,
    val property: PropertySummary,
)
fun PropertyListing.toLightCopy(): PropertyListing {
    return this.copy(
        id = this.id,
        title = this.title,
        agent = this.agent,
        type = this.type,
        price = this.price,
        property = Property(
            city = this.property.city,
            street = this.property.street,
            civicNumber = this.property.civicNumber,
            cap = this.property.cap ?: "",
            country = this.property.country ?: "",
            province = this.property.province ?: "",
            latitude = this.property.latitude,
            longitude = this.property.longitude,
            pois = this.property.pois ?: emptyList(),
            images = emptyList(), // 🔹 foto rimosse
            numberOfRooms = this.property.numberOfRooms,
            numberOfBathrooms = this.property.numberOfBathrooms,
            size = this.property.size,
            energyClass = this.property.energyClass,
            parking = this.property.parking,
            garden = this.property.garden,
            elevator = this.property.elevator,
            gatehouse = this.property.gatehouse,
            balcony = this.property.balcony,
            roof = this.property.roof,
            airConditioning = this.property.airConditioning,
            heatingSystem = this.property.heatingSystem,
            description = this.property.description ?: ""
        )
    )
}