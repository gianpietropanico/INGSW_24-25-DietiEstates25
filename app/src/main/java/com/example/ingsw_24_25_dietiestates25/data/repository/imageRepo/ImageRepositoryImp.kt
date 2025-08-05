package com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo

import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.api.imageApi.ImageApi
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.request.ImageRequest
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val sessionManager: UserSessionManager
) : ImageRepository {

    override suspend fun insertProfilePicture(ownerId: String, profilePicture: String) {
        try {

            Log.d("ImageRepository", "INSERISCO IMMAGINE")
            imageApi.insertProfilePicture(
                ImageRequest(
                    ownerId = ownerId,
                    base64Images = listOf(profilePicture),
                )
            )

            sessionManager.saveProfilePic(profilePicture)

        } catch (e: Exception) {

            Log.e("ImageRepository", "Errore durante l'inserimento immagine: ${e.localizedMessage}")
            throw e
        }
    }

    override suspend fun insertHouseImages(request: ImageRequest) {
        imageApi.insertHouseImages(request)
    }

    override suspend fun getImage(userId: String) {
        try {

            Log.d("ImageRepository", "PRENDO IMMAGINE")
            val profilePicture = imageApi.getImage(userId)

            sessionManager.saveProfilePic(profilePicture)

        } catch (e: Exception) {

            Log.e("ImageRepository", "Errore durante il retrieve dell' immagine: ${e.localizedMessage}")
            throw e
        }

    }
}