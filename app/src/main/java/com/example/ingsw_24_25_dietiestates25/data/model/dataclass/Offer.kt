package com.example.ingsw_24_25_dietiestates25.data.model.dataclass
import kotlinx.serialization.Serializable

@Serializable
data class Offer(
    val id: String,
    val propertyId: String,
    val buyerName: String,
    val agentName: String,
    val messages: MutableList<OfferMessage> = mutableListOf()
)

@Serializable
data class OfferMessage(
    val id: String,
    val senderId: String,
    val timestamp: Long,
    val amount: Double?,
    val accepted: Boolean? = null // null = idle, true = accettata, false = rifiutata
)

@Serializable
data class OfferSummary(
    val amount: Double?,
    val accepted: Boolean?
)