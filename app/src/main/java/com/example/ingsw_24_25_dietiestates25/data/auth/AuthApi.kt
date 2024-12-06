package com.example.ingsw_24_25_dietiestates25.data.auth

import io.ktor.client.statement.HttpResponse

interface AuthApi {

    suspend fun signUp(request: AuthRequest)

    suspend fun signIn(request: AuthRequest): TokenResponse

    suspend fun authenticate(token: String): HttpResponse


}