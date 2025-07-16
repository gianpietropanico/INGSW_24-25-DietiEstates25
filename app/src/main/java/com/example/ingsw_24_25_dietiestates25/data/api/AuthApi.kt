package com.example.ingsw_24_25_dietiestates25.data.api

import com.example.ingsw_24_25_dietiestates25.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.response.TokenResponse

interface AuthApi {
    suspend fun signUp(request: AuthRequest): TokenResponse
    suspend fun signIn(request: AuthRequest): TokenResponse
    suspend fun resetPassword ( request: AuthRequest)
    suspend fun authWithThirdParty(request: AuthRequest): TokenResponse


    suspend fun fetchGitHubState() : String
    suspend fun exchangeGitHubCode(code: String?, state: String?): TokenResponse


}