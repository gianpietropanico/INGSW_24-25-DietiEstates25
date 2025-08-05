package com.example.ingsw_24_25_dietiestates25.data.api.imageApi

import com.example.ingsw_24_25_dietiestates25.model.request.ImageRequest


interface ImageApi {

    suspend fun insertProfilePicture(request: ImageRequest)
    suspend fun insertHouseImages(request: ImageRequest)
    suspend fun getImage(userId: String): String
}