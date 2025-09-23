package com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo
import io.ktor.client.request.accept
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import io.ktor.client.statement.bodyAsText
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.statement.bodyAsText
import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.data.model.request.ImageRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val sessionManager: UserSessionManager
) : ImageRepository {

    private val baseURL = "http://10.0.2.2:8080"
    private val userSessionManager = sessionManager

    override suspend fun insertProfilePicture(ownerIdentifier: String, profilePicture: String, type: String): ApiResult<Unit> {
        return try {
            Log.d("ImageRepository", "INSERISCO IMMAGINE")

            val request = if (ownerIdentifier.contains("@")) {
                ImageRequest(
                    ownerEmail = ownerIdentifier,
                    base64Images = listOf(profilePicture),
                )
            } else {
                ImageRequest(
                    ownerId = ownerIdentifier,
                    base64Images = listOf(profilePicture),
                )
            }

            val response = httpClient.post("$baseURL/user/profile/image") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    if (type == "agency") {
                        Log.d("ImageRepository", "SALVO IMMAGINE AGENCY")
                        userSessionManager.saveAgencyProfilePic(profilePicture)
                    } else {
                        Log.d("ImageRepository", "SALVO IMMAGINE USER")
                        userSessionManager.saveProfilePic(profilePicture)
                    }
                    ApiResult.Success(Unit, "Operation Successfull")
                }
                HttpStatusCode.Conflict -> {
                    val err = response.bodyAsText()
                    Log.e("ImageRepository", "Errore inserimento immagine: $err")
                    ApiResult.Unauthorized("Errore Inserimento immagine: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("ImageRepository", "Errore HTTP ${response.status}: $err")
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Errore Inserimento immagine")
                else -> ApiResult.UnknownError("Errore HTTP ${e.response.status.value}")
            }
        } catch (e: Exception) {
            Log.e("ImageRepository", "Errore generico durante inserimento immagine", e)
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun insertHouseImages(ownerId: String, propertyPicture: String): ApiResult<Unit> {
        return try {
            val request = ImageRequest(
                ownerId = ownerId,
                base64Images = listOf(propertyPicture)
            )

            val response = httpClient.post("$baseURL/house/image") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    ApiResult.Success(Unit, "Immagine casa inserita con successo")
                }
                HttpStatusCode.Conflict -> {
                    val err = response.bodyAsText()
                    Log.e("ImageRepository", "Errore inserimento immagine casa: $err")
                    ApiResult.Unauthorized("Errore inserimento immagine casa: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("ImageRepository", "Errore HTTP ${response.status}: $err")
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Errore inserimento immagine casa")
                else -> ApiResult.UnknownError("Errore HTTP ${e.response.status.value}")
            }
        } catch (e: Exception) {
            Log.e("ImageRepository", "Errore generico durante inserimento immagine casa", e)
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun getImage(userId: String, type: String): ApiResult<Unit> {
        return try {
            Log.d("ImageRepository", "PRENDO IMMAGINE")

            val response = httpClient.get("$baseURL/user/profile/image/$userId") {
                accept(ContentType.Text.Plain)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val profilePicture = response.bodyAsText()

                    if (type == "agency") {
                        Log.d("ImageRepository", "SALVO IMMAGINE AGENCY")
                        userSessionManager.saveAgencyProfilePic(profilePicture)
                    } else {
                        Log.d("ImageRepository", "SALVO IMMAGINE USER")
                        userSessionManager.saveProfilePic(profilePicture)
                    }

                    ApiResult.Success(Unit, "Operation Successfull")
                }
                HttpStatusCode.Conflict -> {
                    val err = response.bodyAsText()
                    Log.e("ImageRepository", "Errore nel retrieve immagine: $err")
                    ApiResult.Unauthorized("Errore nel retrieve immagine: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("ImageRepository", "Errore HTTP ${response.status}: $err")
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Errore nel retrieve immagine")
                else -> ApiResult.UnknownError("Errore HTTP ${e.response.status.value}")
            }
        } catch (e: Exception) {
            Log.e("ImageRepository", "Errore generico durante recupero immagine", e)
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

}