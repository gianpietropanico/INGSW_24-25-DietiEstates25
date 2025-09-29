package com.example.ingsw_24_25_dietiestates25.ui.offerUI

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentMessage
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentSummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferMessage
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferSummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User

data class InboxState (
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val resultMessage: String? = null,
    val localError: Boolean = false,
    val offers : List<Offer> = emptyList(),
    val appointments : List<Appointment> = emptyList(),
    val offerMessages : List<OfferMessage> = emptyList(),
    val appointmentMessages : List<AppointmentMessage> = emptyList(),
    val historyOffers : List<OfferSummary> = emptyList(),
    val historyAppointments : List<AppointmentSummary> = emptyList(),
    val selectedOffer : Offer? = null,
    val selectedAppointment: Appointment? = null,
    val selectedProperty : PropertyListing? = null
)