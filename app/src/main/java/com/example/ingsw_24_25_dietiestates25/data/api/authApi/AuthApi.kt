package com.example.ingsw_24_25_dietiestates25.data.api.authApi

import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.response.TokenResponse

interface AuthApi {
    suspend fun signUp(request: AuthRequest): TokenResponse
    suspend fun signIn(request: AuthRequest): TokenResponse
    suspend fun authWithThirdParty(request: AuthRequest): TokenResponse
    suspend fun sendAgencyRequest(request: AuthRequest)
    suspend fun getLoggedUser(authHeader: String): User

    suspend fun fetchGitHubState() : String
    suspend fun githubOauth(code: String?, state: String?): TokenResponse



}