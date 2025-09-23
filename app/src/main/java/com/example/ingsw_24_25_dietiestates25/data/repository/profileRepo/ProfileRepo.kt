package com.example.ingsw_24_25_dietiestates25.data.repository.profileRepo

import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult

interface ProfileRepo {
    suspend fun resetPassword( oldPassword: String , newPassword: String): ApiResult<Unit>
    suspend fun logout()
    suspend fun updateUserInfo( value: String, type: String): ApiResult<Unit>
}