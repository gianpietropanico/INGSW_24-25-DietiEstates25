package com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo

import com.example.ingsw_24_25_dietiestates25.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.model.request.ImageRequest

interface ImageRepository {

    suspend fun insertProfilePicture(ownerId: String , profilePicture : String)
    suspend fun insertHouseImages(request: ImageRequest)
    suspend fun getImage(userId: String)

}