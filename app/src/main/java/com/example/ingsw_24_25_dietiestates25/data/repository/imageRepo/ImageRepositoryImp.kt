package com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo

import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.api.imageApi.ImageApi
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.request.ImageRequest
import com.example.ingsw_24_25_dietiestates25.model.result.AuthResult
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val sessionManager: UserSessionManager
) : ImageRepository {

    override suspend fun insertProfilePicture(ownerId: String, profilePicture: String): AuthResult<Unit> {
        return try {
            Log.d("ImageRepository", "INSERISCO IMMAGINE")
            imageApi.insertProfilePicture(
                ImageRequest(
                    ownerId = ownerId,
                    base64Images = listOf(profilePicture),
                )
            )

            sessionManager.saveProfilePic(profilePicture)

            AuthResult.Success(Unit, "Operation Successfull")

        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> AuthResult.Unauthorized("Errore Inserimento immagine")
                else -> AuthResult.UnknownError("Errore")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Errore generico")
        }
    }

    override suspend fun insertHouseImages(request: ImageRequest) {
        imageApi.insertHouseImages(request)
    }

    override suspend fun getImage(userId: String): AuthResult<Unit> {
        return try {
            Log.d("ImageRepository", "PRENDO IMMAGINE")
            val profilePicture = imageApi.getImage(userId)
            sessionManager.saveProfilePic(profilePicture)
            AuthResult.Success(Unit, "Operation Successfull")

        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> AuthResult.Unauthorized("Errore nel retrieve immagine")
                else -> AuthResult.UnknownError("Errore")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Errore generico")
        }
    }
}