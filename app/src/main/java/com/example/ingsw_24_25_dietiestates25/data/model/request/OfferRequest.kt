package com.example.ingsw_24_25_dietiestates25.data.model.request

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import kotlinx.serialization.Serializable

@Serializable
data class OfferRequest(
    val property: PropertyListing,
    val buyerUser: User,
    val agent : User,
    val amount: Double
)

@Serializable
data class MessageRequest(
    val offerId: String,
    val sender: User,
    val amount: Double
)
