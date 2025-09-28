package com.example.ingsw_24_25_dietiestates25.data.repository.profileRepo

import android.app.Activity
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.UserActivity
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult

interface ProfileRepo {
    suspend fun resetPassword( oldPassword: String , newPassword: String): ApiResult<Unit>
    suspend fun logout()
    suspend fun updateUserInfo( value: String, type: String): ApiResult<Unit>
    suspend fun getActivities(id: String): ApiResult<List<UserActivity>>
}