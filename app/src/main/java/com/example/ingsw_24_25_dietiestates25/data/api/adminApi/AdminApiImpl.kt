package com.example.ingsw_24_25_dietiestates25.data.api.adminApi


import android.util.Log
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.request.UserInfoRequest
import com.example.ingsw_24_25_dietiestates25.model.response.ListResponse
import com.example.ingsw_24_25_dietiestates25.model.response.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody

import io.ktor.client.statement.bodyAsText
import io.ktor.http.contentType


class AdminApiImpl @Inject constructor (private val httpClient: HttpClient) : AdminApi {

    override suspend fun getAllAgencies(): ListResponse<List<Agency>> {
        val response = httpClient.get("http://10.0.2.2:8080/agency/agencies") {
            accept(ContentType.Application.Json)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            else -> {
                val err = response.bodyAsText()
                throw ResponseException(response, "Errore HTTP ${response.status}: $err")
            }
        }
    }

    override suspend fun decideRequest(request: UserInfoRequest): ListResponse<Unit> {
        val response = httpClient.post("http://10.0.2.2:8080/agency/request-decision") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(request)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            else -> {
                val err = response.bodyAsText()
                throw ResponseException(response, "Errore HTTP ${response.status}: $err")
            }
        }
    }
}

