package com.example.ingsw_24_25_dietiestates25.data.api

import com.example.ingsw_24_25_dietiestates25.model.authenticate.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.authenticate.AuthResponse
import com.example.ingsw_24_25_dietiestates25.model.authenticate.TokenResponse
import com.example.ingsw_24_25_dietiestates25.model.authenticate.User
import io.ktor.client.statement.HttpResponse

interface AuthApi {
    suspend fun signUp(request: AuthRequest)
    suspend fun signIn(request: AuthRequest): TokenResponse
    suspend fun resetPassword ( request: AuthRequest)

    suspend fun authenticate(token: String): HttpResponse
    suspend fun fetchStateKtor() : String
    suspend fun notifyServer(code: String?, state: String?): User

    suspend fun fetchJwtFromServer(code : String): String?
    suspend fun exchangeCodeForJwt(code: String): String?
    suspend fun fetchAuthResponse(code: String): AuthResponse?
}