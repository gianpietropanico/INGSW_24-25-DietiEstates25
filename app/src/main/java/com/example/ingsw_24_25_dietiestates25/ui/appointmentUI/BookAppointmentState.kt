package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import java.time.LocalDate

data class BookAppointmentState(
    val selectedDate: LocalDate? = null,
    val unavailableDates: List<LocalDate> = emptyList(),
    val weatherInfo: String? = null,
    val isLoading: Boolean = false,
    val resultMessage: String? = null,
    val success: Boolean = false
)
