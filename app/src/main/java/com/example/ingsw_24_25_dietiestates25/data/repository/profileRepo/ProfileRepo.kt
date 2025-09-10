package com.example.ingsw_24_25_dietiestates25.data.repository.profileRepo

import com.example.ingsw_24_25_dietiestates25.model.result.AuthResult

interface ProfileRepo {
    suspend fun resetPassword( oldPassword: String , newPassword: String): AuthResult<Unit>
    suspend fun logout()
    suspend fun updateUserInfo( value: String, type: String): AuthResult<Unit>
}