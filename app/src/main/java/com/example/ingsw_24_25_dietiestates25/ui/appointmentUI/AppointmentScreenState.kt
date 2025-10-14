package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import java.time.LocalDate

data class AppointmentScreenState(
    val unavailableDates: Set<LocalDate> = emptySet(),
    val appointments: List<Appointment> = emptyList(),
    val selectedDate: LocalDate? = null,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val resultMessage: String? = null
)