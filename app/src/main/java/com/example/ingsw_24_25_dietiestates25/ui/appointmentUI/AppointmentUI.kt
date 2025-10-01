package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import java.time.LocalDate

data class AppointmentUI(
    val date: LocalDate,
    val time: String,         // es "14:00"
    val propertyTitle: String,
    val bookedBy: String  ,
    val listingId: String
)