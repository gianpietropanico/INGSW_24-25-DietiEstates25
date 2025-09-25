package com.example.ingsw_24_25_dietiestates25.data.model.dataclass


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class Property(
    val city: String,
    val cap: String,
    val country: String,
    val province: String,
    val street: String,
    val civicNumber: String,
    val latitude: Double,
    val longitude: Double,
    val indicators: List<String> = emptyList(),
    val images: List<String> = emptyList(),
    val numberOfRooms: Int,
    val numberOfBathrooms: Int,
    val size: Float,
    val energyClass: EnergyClass,
    val parking: Boolean,
    val garden: Boolean,
    val elevator: Boolean,
    val gatehouse: Boolean,
    val balcony: Boolean,
    val roof: Boolean,
    val airConditioning: Boolean,
    val heatingSystem: Boolean,
    val description: String
)

@Serializable
enum class EnergyClass(val label: String) {
    @SerialName("A")
    A("A"),
    @SerialName("B")
    B("B"),
    @SerialName("C")
    C("C"),
    @SerialName("D")
    D("D"),
    @SerialName("E")
    E("E"),
    @SerialName("F")
    F("F"),
    @SerialName("G")
    G("G")
}