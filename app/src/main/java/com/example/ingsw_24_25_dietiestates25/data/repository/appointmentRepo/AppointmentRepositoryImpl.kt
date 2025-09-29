package com.example.ingsw_24_25_dietiestates25.data.repository.appointmentRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentSummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferSummary
import com.example.ingsw_24_25_dietiestates25.data.model.request.AppointmentRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.OfferRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.data.repository.offerRepo.OfferRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val sessionManager: UserSessionManager
) : AppointmentRepository {

    private val baseURL = "http://10.0.2.2:8080"

    override suspend fun bookAppointment(request: AppointmentRequest): ApiResult<Appointment> {

        return try {
            val response = httpClient.post("$baseURL/appointments/bookappointment") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: Appointment = response.body()
                    ApiResult.Success(body, "Richiesta di appuntamento creato con successo")
                }
                else -> ApiResult.UnknownError("Errore HTTP: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
        }
    }

    override suspend fun getAppointmentsByListing(listingId: String): ApiResult<List<Appointment>> {
        return try {
            val response = httpClient.get("$baseURL/appointments/listingappointments") {
                url { parameters.append("listingId", listingId) }
                accept(ContentType.Application.Json)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val body: List<Appointment> = response.body()
                    ApiResult.Success(body)
                }

                else -> ApiResult.UnknownError("Errore HTTP: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
        }
    }

    override suspend fun getAppointmentsByUser(userId: String): ApiResult<List<Appointment>> {
        return try {
            val response = httpClient.get("$baseURL/appointments/byuser") {
                url { parameters.append("userId", userId) }
                accept(ContentType.Application.Json)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val body: List<Appointment> = response.body()
                    ApiResult.Success(body)
                }

                else -> ApiResult.UnknownError("Errore HTTP: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
        }
    }

    override suspend fun getAllAppointments(): ApiResult<List<AppointmentSummary>> {
        return try {
            val response = httpClient.get("$baseURL/appointments/all") {
                accept(ContentType.Application.Json)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val body: List<AppointmentSummary> = response.body()
                    ApiResult.Success(body)
                }

                else -> ApiResult.UnknownError("Errore HTTP: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
        }
    }

    override suspend fun acceptAppointment(appointmentId: String): ApiResult<Unit> {
        return try {
            val response = httpClient.post("$baseURL/appointments/accept") {
                url { parameters.append("appointmentId", appointmentId) }
                accept(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                ApiResult.Success(Unit, "Appuntamento accettato con successo")
            } else {
                ApiResult.UnknownError("Errore HTTP: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
        }
    }

    override suspend fun rejectAppointment(appointmentId: String): ApiResult<Unit> {
        return try {
            val response = httpClient.post("$baseURL/appointments/decline") {
                url { parameters.append("appointmentId", appointmentId) }
                accept(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                ApiResult.Success(Unit, "Appuntamento rifiutata con successo")
            } else {
                ApiResult.UnknownError("Errore HTTP: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
        }
    }
}