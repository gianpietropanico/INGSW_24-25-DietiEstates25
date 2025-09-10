package com.example.ingsw_24_25_dietiestates25.data.api.propertyListingApi


import com.example.ingsw_24_25_dietiestates25.model.dataclass.PropertyListing
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class PropertyListingApiImpl @Inject constructor (private val httpClient: HttpClient) : PropertyListingApi {

    override suspend fun addPropertyListing(propertyListing: PropertyListing){
        return httpClient.post("http://10.0.2.2:8080/propertiesListing/addpropertylisting") {
            contentType(ContentType.Application.Json)
            setBody(propertyListing)
        }.body()
    }

    override suspend fun getPropertiesListingByAgent(agentEmail: String): List<PropertyListing>{
        return httpClient.get("http://10.0.2.2:8080/propertiesListing/getpropertieslistingbyemail") {
            contentType(ContentType.Application.Json)
            setBody(agentEmail)
        }.body()
    }

    override suspend fun getAllListings(): List<PropertyListing> {
        return try {
            httpClient.get("http://10.0.2.2:8080/propertylisting/getallpropertieslisting")
                .body()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getListingsWithinRadius(
        lat: Double,
        lon: Double,
        radius: Double
    ): List<PropertyListing> {
        return try {
            httpClient.get("http://10.0.2.2:8080/propertylisting/getpropertieslistingwithinradius") {
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("radius", radius)
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}