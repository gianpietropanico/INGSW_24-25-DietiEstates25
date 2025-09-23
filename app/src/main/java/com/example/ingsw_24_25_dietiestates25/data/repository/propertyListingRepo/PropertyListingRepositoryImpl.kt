package com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo

import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import io.ktor.client.statement.bodyAsText
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode



class PropertyListingRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val sessionManager: UserSessionManager
) : PropertyListingRepository {

    private val baseURL = "http://10.0.2.2:8080"
    private val userSessionManager = sessionManager

    override suspend fun addPropertyListing(propertyListing: PropertyListing): ApiResult<Unit> {
        return try {
            val response = httpClient.post("$baseURL/propertiesListing/addpropertylisting") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(propertyListing)
            }

            return when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    ApiResult.Success(Unit, "Property inserita con successo")
                }
                HttpStatusCode.Conflict -> {
                    val err = response.bodyAsText()
                    ApiResult.Unauthorized("Inserimento fallito: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Inserimento fallito")
                else -> ApiResult.UnknownError("Errore HTTP ${e.response.status.value}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun getPropertiesListingByAgent(agentEmail: String): ApiResult<List<PropertyListing>> {
        return try {
            val response = httpClient.get("$baseURL/propertiesListing/getpropertieslistingbyemail") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(agentEmail)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val listings: List<PropertyListing> = response.body()
                    ApiResult.Success(listings, "Lista proprietà recuperata con successo")
                }
                HttpStatusCode.NotFound -> {
                    ApiResult.UnknownError("Nessuna proprietà trovata per l'agente $agentEmail")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.NotFound -> ApiResult.UnknownError("Nessuna proprietà trovata per l'agente $agentEmail")
                else -> ApiResult.UnknownError("Errore HTTP ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun getAllListings(): ApiResult<List<PropertyListing>> {
        return try {
            val response = httpClient.get("$baseURL/propertylisting/getallpropertieslisting") {
                accept(ContentType.Application.Json)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val listings: List<PropertyListing> = response.body()
                    ApiResult.Success(listings, "Lista di tutte le proprietà recuperata con successo")
                }
                HttpStatusCode.NotFound -> {
                    ApiResult.UnknownError("Nessuna proprietà trovata")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.NotFound -> ApiResult.UnknownError("Nessuna proprietà trovata")
                else -> ApiResult.UnknownError("Errore HTTP ${e.response.status.value}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun getListingsWithinRadius(
        lat: Double,
        lon: Double,
        radius: Double
    ): ApiResult<List<PropertyListing>> {
        return try {
            val response = httpClient.get("$baseURL/propertylisting/getpropertieslistingwithinradius") {
                accept(ContentType.Application.Json)
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("radius", radius)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val listings: List<PropertyListing> = response.body()
                    ApiResult.Success(listings, "Proprietà recuperate con successo nel raggio indicato")
                }
                HttpStatusCode.NotFound -> {
                    ApiResult.UnknownError("Nessuna proprietà trovata entro il raggio di $radius km")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.NotFound -> ApiResult.UnknownError("Nessuna proprietà trovata entro il raggio di $radius km")
                else -> ApiResult.UnknownError("Errore HTTP ${e.response.status.value}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

}