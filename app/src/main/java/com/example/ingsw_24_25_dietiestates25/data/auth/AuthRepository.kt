package com.example.ingsw_24_25_dietiestates25.data.auth

import com.example.ingsw_24_25_dietiestates25.data.model.User

interface AuthRepository {
    suspend fun signUp(email: String, password: String):AuthResult<Unit>
    suspend fun signIn(email:String, password: String):AuthResult<Unit>
    suspend fun authenticate():AuthResult<Unit>
    suspend fun logout()
    suspend fun fetchState(): AuthResult<String>
    suspend fun notifyServer(code: String?, state: String?):  AuthResult<User>

    suspend fun fetchJwtFromServer(code : String): String?
    suspend fun exchangeCodeForJwt(code: String): String?
    suspend fun fetchAuthResponse(code: String): AuthResponse?
}