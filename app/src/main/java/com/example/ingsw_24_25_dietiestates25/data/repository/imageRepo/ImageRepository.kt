package com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo

import android.content.Context
import android.net.Uri
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult

interface ImageRepository {

    suspend fun insertProfilePicture(ownerIdentifier: String , profilePicture : String, type: String) : ApiResult<Unit>
    suspend fun insertHouseImages(ownerId: String, propertyPicture :List<String>): ApiResult<Unit>
    suspend fun getImage(userId: String, type : String): ApiResult<Unit>
    suspend fun uploadImages(imageUris: List<Uri>, context: Context): List<String>
    suspend fun uploadImage(uri: Uri, context: Context): String?

}