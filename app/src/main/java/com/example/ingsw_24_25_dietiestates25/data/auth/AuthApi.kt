package com.example.ingsw_24_25_dietiestates25.data.auth

import com.example.ingsw_24_25_dietiestates25.data.model.User
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

interface AuthApi {

    suspend fun signUp(request: AuthRequest)

    suspend fun signIn(request: AuthRequest): TokenResponse

    suspend fun authenticate(token: String): HttpResponse

    suspend fun fetchStateKtor() : String

    suspend fun notifyServer(code: String?, state: String?): User
}