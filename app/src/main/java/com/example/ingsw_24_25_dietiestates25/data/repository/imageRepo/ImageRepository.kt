package com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo

import com.example.ingsw_24_25_dietiestates25.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.model.request.ImageRequest
import com.example.ingsw_24_25_dietiestates25.model.result.AuthResult

interface ImageRepository {

    suspend fun insertProfilePicture(ownerId: String , profilePicture : String) : AuthResult<Unit>
    suspend fun insertHouseImages(ownerId: String, propertyPicture :String)
    suspend fun getImage(userId: String): AuthResult<Unit>
}