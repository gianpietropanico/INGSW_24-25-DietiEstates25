package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import java.time.LocalDate

data class AppointmentScreenState(
    val unavailableDates: List<LocalDate> = emptyList(),
    val selectedDate: LocalDate? = null,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val resultMessage: String? = null
)