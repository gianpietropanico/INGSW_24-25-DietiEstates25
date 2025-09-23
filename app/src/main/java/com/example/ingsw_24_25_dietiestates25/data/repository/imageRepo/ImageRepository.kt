package com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo

import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult

interface ImageRepository {

    suspend fun insertProfilePicture(ownerIdentifier: String , profilePicture : String, type: String) : ApiResult<Unit>
    suspend fun insertHouseImages(ownerId: String, propertyPicture :String): ApiResult<Unit>
    suspend fun getImage(userId: String, type : String): ApiResult<Unit>

}