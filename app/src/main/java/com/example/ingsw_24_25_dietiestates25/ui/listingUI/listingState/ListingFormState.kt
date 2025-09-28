package com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type

data class ListingFormState(
    val title: String = "",
    val type: Type = Type.RENT,
    val price: String = "",
    val city: String = "",
    val cap: String = "",
    val country: String = "",
    val province: String = "",
    val street: String = "",
    val civicNumber: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val numberOfRooms: String = "",
    val numberOfBathrooms: String = "",
    val size: String = "",
    val energyClass: EnergyClass = EnergyClass.A,
    val parking: Boolean = false,
    val garden: Boolean = false,
    val elevator: Boolean = false,
    val gatehouse: Boolean = false,
    val balcony: Boolean = false,
    val roof: Boolean = false,
    val airConditioning: Boolean = false,
    val heatingSystem: Boolean = false,
    val description: String = ""
)