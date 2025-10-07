package com.example.ingsw_24_25_dietiestates25.data.model.request

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.ui.utils.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class AppointmentRequest(
    val listingId: String,
    val userId: String,
    val agentId: String,
    val date: String
)

@Serializable
data class AppointmentMessageRequest(
    val appointmentId: String,
    val senderId: String,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate
)