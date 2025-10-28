package com.example.ingsw_24_25_dietiestates25.data.repository.appointmentRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentSummary
import com.example.ingsw_24_25_dietiestates25.data.model.request.AppointmentRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult

interface AppointmentRepository {
    suspend fun getAppointmentsByUser(userId: String): ApiResult<List<Appointment>>
    suspend fun getAllAppointments(): ApiResult<List<AppointmentSummary>>
    suspend fun acceptAppointment(appointmentId: String): ApiResult<Unit>
    suspend fun declineAppointment(appointmentId: String): ApiResult<Unit>

    suspend fun bookAppointment(request: AppointmentRequest): ApiResult<Appointment>
    suspend fun getAppointmentsByListing(listingId: String, userId: String? = null): ApiResult<List<Appointment>>
}