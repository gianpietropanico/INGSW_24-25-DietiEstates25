package com.example.ingsw_24_25_dietiestates25.data.model.dataclass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Offer(
    val id: String,
    val listing: PropertyListing,
    val buyerUser: User,
    val agent : User,
    val messages: MutableList<OfferMessage> = mutableListOf()
)

@Serializable
data class OfferMessage(
    val id: String,
    val sender: User,
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
    val timestamp: Long,
    val status: OfferStatus
)