package com.example.ingsw_24_25_dietiestates25.data.model.request

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.ListingSummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.ui.utils.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class AppointmentRequest(
    val listing: ListingSummary,
    val user: User,
    val agent: User,
    val date: String
)

@Serializable
data class OfferAppointmentRequest(
    val property: PropertyListing,
    val buyerUser: User,
    val agent : User,
    val appointment: Appointment
)