package com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo

import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.api.imageApi.ImageApi
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.request.ImageRequest
import com.example.ingsw_24_25_dietiestates25.model.result.ApiResult
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val sessionManager: UserSessionManager
) : ImageRepository {

    override suspend fun insertProfilePicture(ownerIdentifier : String, profilePicture: String): ApiResult<Unit> {
        return try {
            Log.d("ImageRepository", "INSERISCO IMMAGINE")

            if( ownerIdentifier.contains("@")){
                imageApi.insertProfilePicture(
                    ImageRequest(
                        ownerEmail = ownerIdentifier,
                        base64Images = listOf(profilePicture),
                    )
                )
            }else{
                imageApi.insertProfilePicture(
                    ImageRequest(
                        ownerId = ownerIdentifier,
                        base64Images = listOf(profilePicture),
                    )
                )
            }

            sessionManager.saveProfilePic(profilePicture)

            ApiResult.Success(Unit, "Operation Successfull")

        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Errore Inserimento immagine")
                else -> ApiResult.UnknownError("Errore")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico")
        }
    }

    override suspend fun insertHouseImages(ownerId: String, propertyPicture :String) {
        imageApi.insertHouseImages(
            ImageRequest(
                ownerId = ownerId,
                base64Images = listOf(propertyPicture)))
    }

    override suspend fun getImage(userId: String): ApiResult<Unit> {
        return try {
            Log.d("ImageRepository", "PRENDO IMMAGINE")
            val profilePicture = imageApi.getImage(userId)

            sessionManager.saveProfilePic(profilePicture)
            ApiResult.Success(Unit, "Operation Successfull")

        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Errore nel retrieve immagine")
                else -> ApiResult.UnknownError("Errore")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico")
        }
    }
}