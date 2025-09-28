package com.example.ingsw_24_25_dietiestates25.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class OfferRequest(
    val propertyId: String,
    val buyerName: String,
    val agentName: String,
    val amount: Double
)

@Serializable
data class MessageRequest(
    val offerId: String,
    val senderId: String,
    val amount: Double
)
