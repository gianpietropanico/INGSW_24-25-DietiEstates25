package com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo

import com.example.ingsw_24_25_dietiestates25.model.result.ApiResult

interface ImageRepository {

    suspend fun insertProfilePicture(ownerId: String , profilePicture : String) : ApiResult<Unit>
    suspend fun insertHouseImages(ownerId: String, propertyPicture :String)
    suspend fun getImage(userId: String): ApiResult<Unit>

}