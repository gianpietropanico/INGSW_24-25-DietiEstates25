package com.example.ingsw_24_25_dietiestates25.data.api.profileApi

import com.example.ingsw_24_25_dietiestates25.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.request.UserInfoRequest

interface ProfileApi {
    suspend fun resetPassword ( request: AuthRequest)
    suspend fun updateUserInfo ( request : UserInfoRequest)
}