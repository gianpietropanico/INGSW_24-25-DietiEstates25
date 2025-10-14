package com.example.ingsw_24_25_dietiestates25.data.model.dataclass

import kotlinx.serialization.Serializable
import com.example.ingsw_24_25_dietiestates25.ui.utils.LocalDateSerializer
import kotlinx.serialization.SerialName
import java.time.LocalDate

@Serializable
data class Appointment(
    val id: String,
    val listing :ListingSummary,
    val user: User,
    val agent: User,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val status: AppointmentStatus = AppointmentStatus.PENDING,
    val appointmentMessages: List<AppointmentMessage> = emptyList()
)

@Serializable
enum class AppointmentStatus {
    @SerialName("PENDING")
    PENDING,
    @SerialName("ACCEPTED")
    ACCEPTED,
    @SerialName("REJECTED")
    REJECTED
}
@Serializable
data class AppointmentMessage(
    val id: String,
    val senderId: String,
    val timestamp: Long,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val accepted: Boolean? = null,
    val text: String? = null
)

@Serializable
data class AppointmentSummary(
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val status: Boolean?
)

