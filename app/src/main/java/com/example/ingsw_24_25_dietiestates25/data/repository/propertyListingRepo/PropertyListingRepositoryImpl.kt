package com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo

import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.request.PropertySearchRequest
import com.example.ingsw_24_25_dietiestates25.data.model.response.ListResponse
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
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Agency


class PropertyListingRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val sessionManager: UserSessionManager
) : PropertyListingRepository {

    private val baseURL = "http://10.0.2.2:8080"

    override suspend fun getListingAgency(listingId: String): ApiResult<Agency> {
        return try {

            val response = httpClient.get("$baseURL/propertylisting/getlistingagency/$listingId") {
                accept(ContentType.Application.Json)
            }

            return when (response.status) {

                HttpStatusCode.OK -> {
                    val apiResponse: ListResponse<Agency> = response.body()
                    val agency = apiResponse.data!!

                    Log.d("ListingRepo", "Agency: $agency")

                    ApiResult.Success(agency, apiResponse.message ?: "Agenzia recuperata con successo")
                }

                HttpStatusCode.NotFound -> {
                    val err = response.bodyAsText()
                    ApiResult.NotFound("Agenzia non trovata: $err")
                }

                HttpStatusCode.Unauthorized -> {
                    ApiResult.Unauthorized("Accesso non autorizzato per questa risorsa")
                }

                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }

        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.NotFound -> ApiResult.NotFound("Agenzia non trovata")
                HttpStatusCode.Unauthorized -> ApiResult.Unauthorized("Non autorizzato")
                else -> ApiResult.UnknownError("Errore HTTP ${e.response.status.value}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }


    override suspend fun addPropertyListing(propertyListing: PropertyListing): ApiResult<String> {
        return try {


            val response = httpClient.post("$baseURL/propertylisting/addpropertylisting") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(propertyListing)
            }

            return when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val id: String = response.body()
                    Log.d("ListingRepo", id)

                    ApiResult.Success(id, "Operazione completata con successo")
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
            val response = httpClient.get("$baseURL/propertylisting/getpropertieslistingbyemail") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(agentEmail)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ListResponse<List<PropertyListing>> = response.body()
                    if (body.success && body.data != null) {
                        ApiResult.Success(body.data)
                    } else {
                        ApiResult.UnknownError(body.message ?: "Errore sconosciuto dal server")
                    }
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

    override suspend fun getListingById(id: String): ApiResult<PropertyListing> {
        return try {
            val response = httpClient.get("$baseURL/propertylisting/$id") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val listing: PropertyListing = response.body()
                    ApiResult.Success(listing, "Proprietà recuperata con successo")
                }
                HttpStatusCode.NotFound -> {
                    ApiResult.UnknownError("Proprietà con id $id non trovata")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    override suspend fun searchProperties(type: String, location: String): ApiResult<List<PropertyListing>> {
        return try {
            val response = httpClient.get("$baseURL/propertylisting/search") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                parameter("type", type)
                parameter("city", location)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val listings: ListResponse<List<PropertyListing>> = response.body()
                    if (listings.success && listings.data != null) {
                        ApiResult.Success(listings.data)
                    } else {
                        ApiResult.UnknownError(listings.message ?: "Errore sconosciuto dal server")
                    }
                }
                HttpStatusCode.NotFound -> ApiResult.UnknownError("Nessuna proprietà trovata")
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun searchWithFilters(filters: PropertySearchRequest): ApiResult<List<PropertyListing>> {
        return try {
            val response = httpClient.post("$baseURL/propertylisting/searchWithFilters") {
                contentType(ContentType.Application.Json)
                setBody(filters)
                accept(ContentType.Application.Json)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val listings: List<PropertyListing> = response.body()
                    ApiResult.Success(listings)
                }
                HttpStatusCode.NotFound -> ApiResult.UnknownError("Nessuna proprietà trovata")
                else -> ApiResult.UnknownError("Errore HTTP ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

}