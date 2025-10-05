package com.example.ingsw_24_25_dietiestates25.data.model.dataclass
import kotlinx.serialization.SerialName
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
    val senderName: String,
    val timestamp: Long,
    val amount: Double?,
    val status: OfferStatus?
)
@Serializable
enum class OfferStatus {
    @SerialName("PENDING")
    PENDING,
    @SerialName("ACCEPTED")
    ACCEPTED,
    @SerialName("REJECTED")
    REJECTED
}
@Serializable
data class OfferSummary(
    val amount: Double?,
    val status: OfferStatus
)