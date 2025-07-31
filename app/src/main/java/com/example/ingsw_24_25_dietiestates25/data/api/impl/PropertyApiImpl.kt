package com.example.ingsw_24_25_dietiestates25.data.api.impl

import com.example.ingsw_24_25_dietiestates25.data.api.PropertyApi
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Property
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject

class PropertyApiImpl @Inject constructor (private val httpClient: HttpClient) : PropertyApi {

    override suspend fun addProperty(property: Property): Property {
        return httpClient.post("http://10.0.2.2:8080/properties/upload") {
            contentType(ContentType.Application.Json)
            setBody(property)
        }.body()
    }
}