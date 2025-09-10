package com.example.ingsw_24_25_dietiestates25.model.dataclass


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
    val propertyPicture: String? = null,
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

enum class EnergyClass(val label: String) {
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E"),
    F("F"),
    G("G")
}