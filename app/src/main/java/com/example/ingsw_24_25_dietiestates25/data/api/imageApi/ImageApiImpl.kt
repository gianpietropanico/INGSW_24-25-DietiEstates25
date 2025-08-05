package com.example.ingsw_24_25_dietiestates25.data.api.imageApi

import com.example.ingsw_24_25_dietiestates25.model.request.ImageRequest
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import javax.inject.Inject
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode


class ImageApiImpl  @Inject constructor(
    private val client: HttpClient
): ImageApi {

    override suspend fun insertProfilePicture(request: ImageRequest) {
        client.post("http://10.0.2.2:8080/user/profile/image") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    override suspend fun insertHouseImages(request: ImageRequest) {
        client.post("http://10.0.2.2:8080/house/image") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    override suspend fun getImage(userId: String): String {
        val response = client.get("http://10.0.2.2:8080/user/profile/image/$userId") {
            accept(ContentType.Text.Plain)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.bodyAsText()
            else -> throw ResponseException(response, "Errore nel recupero immagine")
        }
    }
}
