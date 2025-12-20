package com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo
import android.content.Context
import android.net.Uri
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
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
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
            Log.d("ImageRepository", "INSERTING IMAGE")

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
                        Log.d("ImageRepository", "SAVING AGENCY IMAGE")
                        userSessionManager.saveAgencyProfilePic(profilePicture)
                    } else {
                        Log.d("ImageRepository", "SAVING USER IMAGE")
                        userSessionManager.saveProfilePic(profilePicture)
                    }
                    ApiResult.Success(Unit, "Operation Successful")
                }
                HttpStatusCode.Conflict -> {
                    val err = response.bodyAsText()
                    Log.e("ImageRepository", "Image upload error: $err")
                    ApiResult.Unauthorized("Image upload error: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("ImageRepository", "HTTP Error ${response.status}: $err")
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Image upload error")
                else -> ApiResult.UnknownError("HTTP Error ${e.response.status.value}")
            }
        } catch (e: Exception) {
            Log.e("ImageRepository", "Generic error during image upload", e)
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

    override suspend fun insertHouseImages(ownerId: String, propertyPicture: List<String>): ApiResult<Unit> {
        return try {
            val request = ImageRequest(
                ownerId = ownerId,
                base64Images = propertyPicture
            )

            val response = httpClient.post("$baseURL/house/image") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    ApiResult.Success(Unit, "House image successfully inserted")
                }
                HttpStatusCode.Conflict -> {
                    val err = response.bodyAsText()
                    Log.e("ImageRepository", "House image upload error: $err")
                    ApiResult.Unauthorized("House image upload error: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("ImageRepository", "HTTP Error ${response.status}: $err")
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("House image upload error")
                else -> ApiResult.UnknownError("HTTP Error ${e.response.status.value}")
            }
        } catch (e: Exception) {
            Log.e("ImageRepository", "Generic error during house image upload", e)
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

    override suspend fun getImage(userId: String, type: String): ApiResult<Unit> {
        return try {
            Log.d("ImageRepository", "FETCHING IMAGE")

            val response = httpClient.get("$baseURL/user/profile/image/$userId") {
                accept(ContentType.Text.Plain)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val profilePicture = response.bodyAsText()

                    if (type == "agency") {
                        Log.d("ImageRepository", "SAVING AGENCY IMAGE")
                        userSessionManager.saveAgencyProfilePic(profilePicture)
                    } else {
                        Log.d("ImageRepository", "SAVING USER IMAGE")
                        userSessionManager.saveProfilePic(profilePicture)
                    }

                    ApiResult.Success(Unit, "Operation Successful")
                }
                HttpStatusCode.Conflict -> {
                    val err = response.bodyAsText()
                    Log.e("ImageRepository", "Image retrieve error: $err")
                    ApiResult.Unauthorized("Image retrieve error: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("ImageRepository", "HTTP Error ${response.status}: $err")
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Image retrieve error")
                else -> ApiResult.UnknownError("HTTP Error ${e.response.status.value}")
            }
        } catch (e: Exception) {
            Log.e("ImageRepository", "Generic error during image retrieve", e)
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

    override suspend fun uploadImage(uri: Uri, context: Context): String? {
        return try {
            val response: List<String> = uploadImages(listOf(uri), context)
            response.firstOrNull()
        } catch (e: Exception) {
            Log.e("UploadImage", "Image upload error", e)
            null
        }
    }

    override suspend fun uploadImages(imageUris: List<Uri>, context: Context): List<String> {
        val urls = mutableListOf<String>()

        for (uri in imageUris) {
            val fileBytes = context.contentResolver.openInputStream(uri)?.readBytes() ?: continue
            val fileName = uri.lastPathSegment ?: "image.jpg"

            try {
                val response = httpClient.submitFormWithBinaryData(
                    url = "$baseURL/images/upload",
                    formData = formData {
                        append("file", fileBytes, Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=$fileName")
                        })
                    }
                )

                if (response.status == HttpStatusCode.OK) {
                    val uploaded: List<String> = response.body()
                    urls.addAll(uploaded)
                } else {
                    val err = response.bodyAsText()
                    throw Exception("Image upload error: $err")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return urls
    }
}