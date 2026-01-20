package com.example.ingsw_24_25_dietiestates25.data.repository.offerRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferSummary
import com.example.ingsw_24_25_dietiestates25.data.model.request.AppointmentRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.MessageRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.OfferAppointmentRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.OfferRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post

import io.ktor.http.isSuccess
class OfferRepositoryImp @Inject constructor(
    private val httpClient: HttpClient,
    private val sessionManager: UserSessionManager
) : OfferRepository {

    private val baseURL = "http://84.8.252.211:8080/"

    override suspend fun createAppointmentOffer(
        request: OfferAppointmentRequest
    ): ApiResult<Offer> {

        return try {
            val response = httpClient.post("$baseURL/offers/makeappointmentoffer") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.Created -> {
                    val offer: Offer = response.body()
                    ApiResult.Success(offer, "Offer successfully created")
                }

                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError(
                        "HTTP Error ${response.status.value}: $err"
                    )
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Network error: ${e.localizedMessage}")
        }
    }


    override suspend fun createOffer(request: OfferRequest): ApiResult<Offer?> {

        return try {
            val response = httpClient.post("$baseURL/offers/makeoffer") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.Created -> {
                    val offer: Offer = response.body()
                    ApiResult.Success(offer, "Offer successfully created")
                }
                HttpStatusCode.OK -> {
                    val msg: String = response.bodyAsText()
                    ApiResult.Success(null, msg)
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun makeOffer(request: MessageRequest): ApiResult<Unit> {
        return try {
            val response = httpClient.post("$baseURL/offers/message") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                ApiResult.Success(Unit, "Message added successfully")
            } else {
                val err = response.bodyAsText()
                ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun loadOfferChat(offerId: String): ApiResult<Offer> {
        return try {
            val response = httpClient.get("$baseURL/offers/single") {
                url { parameters.append("offerId", offerId) }
                accept(ContentType.Application.Json)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val body: Offer = response.body()
                    ApiResult.Success(body)
                }
                else -> ApiResult.UnknownError("HTTP Error: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun acceptOffer(offerId: String): ApiResult<Unit> {
        return try {
            val response = httpClient.post("$baseURL/offers/accept") {
                url { parameters.append("offerId", offerId) }
                accept(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                ApiResult.Success(Unit, "Offer successfully accepted")
            } else {
                ApiResult.UnknownError("HTTP Error: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun declineOffer(offerId: String): ApiResult<Unit> {
        return try {
            val response = httpClient.post("$baseURL/offers/decline") {
                url { parameters.append("offerId", offerId) }
                accept(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                ApiResult.Success(Unit, "Offer successfully declined")
            } else {
                ApiResult.UnknownError("HTTP Error: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun getOffersSummary(propertyId: String): ApiResult<List<OfferSummary>> {
        return try {
            val response = httpClient.get("$baseURL/offers/summary") {
                url { parameters.append("propertyId", propertyId) }
                accept(ContentType.Application.Json)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val body: List<OfferSummary> = response.body()
                    ApiResult.Success(body)
                }
                else -> ApiResult.UnknownError("HTTP Error: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun getOffersByUser(userId: String): ApiResult<List<Offer>> {
        return try {
            val response = httpClient.get("$baseURL/offers") {
                url { parameters.append("userId", userId) }
                accept(ContentType.Application.Json)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val body: List<Offer> = response.body()
                    ApiResult.Success(body)
                }
                else -> ApiResult.UnknownError("HTTP Error: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun getOffer(propertyId: String, userId: String): ApiResult<Offer?> {
        return try {
            val response = httpClient.get("$baseURL/offers/singlebyuser") {
                url {
                    parameters.append("propertyId", propertyId)
                    parameters.append("userId", userId)
                }
                accept(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.OK -> ApiResult.Success(response.body<Offer>())
                HttpStatusCode.NotFound -> ApiResult.UnknownError(null)
                else -> ApiResult.UnknownError("HTTP Error: ${response.status.value}")
            }

        } catch (e: Exception) {
            ApiResult.UnknownError("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun getAgentNameByEmail(email: String): ApiResult<String> {
        return try {
            val response = httpClient.get("$baseURL/agents/name") {
                url {
                    parameters.append("email", email)
                }
                accept(ContentType.Application.Json)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val agentName = response.body<String>()
                    ApiResult.Success(agentName, "Agent successfully found")
                }
                HttpStatusCode.NotFound -> {
                    ApiResult.UnknownError("No agent found with email $email")
                }
                else -> {
                    ApiResult.UnknownError("Server error: ${response.status}")
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Error during request: ${e.localizedMessage}")
        }
    }
}