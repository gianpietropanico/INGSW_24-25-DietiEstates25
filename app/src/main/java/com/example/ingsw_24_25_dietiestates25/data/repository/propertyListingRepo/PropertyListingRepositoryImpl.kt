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

    private val baseURL = "http://84.8.252.211:8080/"

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

                    ApiResult.Success(agency, apiResponse.message ?: "Agency retrieved successfully")
                }

                HttpStatusCode.NotFound -> {
                    val err = response.bodyAsText()
                    ApiResult.NotFound("Agency not found: $err")
                }

                HttpStatusCode.Unauthorized -> {
                    ApiResult.Unauthorized("Unauthorized access to this resource")
                }

                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }

        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.NotFound -> ApiResult.NotFound("Agency not found")
                HttpStatusCode.Unauthorized -> ApiResult.Unauthorized("Unauthorized")
                else -> ApiResult.UnknownError("HTTP Error ${e.response.status.value}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
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

                    ApiResult.Success(id, "Operation completed successfully")
                }
                HttpStatusCode.Conflict -> {
                    val err = response.bodyAsText()
                    ApiResult.Unauthorized("Insert failed: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Insert failed")
                else -> ApiResult.UnknownError("HTTP Error ${e.response.status.value}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
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
                        ApiResult.UnknownError(body.message ?: "Unknown server error")
                    }
                }
                HttpStatusCode.NotFound -> {
                    ApiResult.UnknownError("No properties found for agent $agentEmail")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.NotFound -> ApiResult.UnknownError("No properties found for agent $agentEmail")
                else -> ApiResult.UnknownError("HTTP Error ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
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
                    ApiResult.Success(listing, "Property retrieved successfully")
                }
                HttpStatusCode.NotFound -> {
                    ApiResult.UnknownError("Property with id $id not found")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
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
                    ApiResult.Success(listings, "List of all properties retrieved successfully")
                }
                HttpStatusCode.NotFound -> {
                    ApiResult.UnknownError("No properties found")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.NotFound -> ApiResult.UnknownError("No properties found")
                else -> ApiResult.UnknownError("HTTP Error ${e.response.status.value}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
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
                    ApiResult.Success(listings, "Properties retrieved successfully within the specified radius")
                }
                HttpStatusCode.NotFound -> {
                    ApiResult.UnknownError("No properties found within a $radius km radius")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.NotFound -> ApiResult.UnknownError("No properties found within a $radius km radius")
                else -> ApiResult.UnknownError("HTTP Error ${e.response.status.value}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
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
                        ApiResult.UnknownError(listings.message ?: "Unknown server error")
                    }
                }
                HttpStatusCode.NotFound -> ApiResult.UnknownError("No properties found")
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
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
                HttpStatusCode.NotFound -> ApiResult.UnknownError("No properties found")
                else -> ApiResult.UnknownError("HTTP Error ${response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

}