package com.example.ingsw_24_25_dietiestates25.data.repository.offerRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferSummary
import com.example.ingsw_24_25_dietiestates25.data.model.request.MessageRequest
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

    private val baseURL = "http://10.0.2.2:8080"

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
                    ApiResult.Success(offer, "Offerta creata con successo")
                }
                HttpStatusCode.OK -> {
                    val msg: String = response.bodyAsText()
                    ApiResult.Success(null, msg) // o gestisci diversamente
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
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
                ApiResult.Success(Unit, "Messaggio aggiunto con successo")
            } else {
                val err = response.bodyAsText()
                ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
        }
    }

    override suspend fun loadOfferChat(offerId : String): ApiResult<Offer>{
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
                else -> ApiResult.UnknownError("Errore HTTP: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
        }
    }

    override suspend fun acceptOffer(offerId: String): ApiResult<Unit> {
        return try {
            val response = httpClient.post("$baseURL/offers/accept") {
                url { parameters.append("offerId", offerId) }
                accept(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                ApiResult.Success(Unit, "Offerta accettata con successo")
            } else {
                ApiResult.UnknownError("Errore HTTP: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
        }
    }

    override suspend fun declineOffer(offerId: String): ApiResult<Unit> {
        return try {
            val response = httpClient.post("$baseURL/offers/decline") {
                url { parameters.append("offerId", offerId) }
                accept(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                ApiResult.Success(Unit, "Offerta rifiutata con successo")
            } else {
                ApiResult.UnknownError("Errore HTTP: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
        }
    }

    override suspend fun getOffersSummary(propertyId: String): ApiResult<List<OfferSummary>>{
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
                else -> ApiResult.UnknownError("Errore HTTP: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
        }
    }

    override suspend fun getOffersByUser(userName: String): ApiResult<List<Offer>> {
        return try {
            val response = httpClient.get("$baseURL/offers") {
                url { parameters.append("userName", userName) }
                accept(ContentType.Application.Json)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val body: List<Offer> = response.body()
                    ApiResult.Success(body)
                }
                else -> ApiResult.UnknownError("Errore HTTP: ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
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
                else -> ApiResult.UnknownError("Errore HTTP: ${response.status.value}")
            }

        } catch (e: Exception) {
            ApiResult.UnknownError("Errore rete: ${e.localizedMessage}")
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
                    ApiResult.Success(agentName, "Agente trovato con successo")
                }
                HttpStatusCode.NotFound -> {
                    ApiResult.UnknownError("Nessun agente trovato con email $email")
                }
                else -> {
                    ApiResult.UnknownError("Errore server: ${response.status}")
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore durante la richiesta: ${e.localizedMessage}")
        }
    }
}
