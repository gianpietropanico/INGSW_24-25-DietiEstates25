package com.example.ingsw_24_25_dietiestates25.data.auth

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AgencyRepository(private val httpClient: HttpClient) {

    suspend fun registerAgency(agencyName: String, email: String): Boolean {
        return try {
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8080/addadminagency") {
                contentType(ContentType.Application.Json)
                setBody(RegistrationRequest(agencyName, email))
            }
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            false
        }
    }
}