package com.example.ingsw_24_25_dietiestates25.data.repository

import com.example.ingsw_24_25_dietiestates25.model.authenticate.AuthResult
import com.example.ingsw_24_25_dietiestates25.model.authenticate.User

interface AuthRepository {
    suspend fun signUp(email: String, password: String): AuthResult<Unit>
    suspend fun signIn(email:String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
    suspend fun logout()
    suspend fun fetchState(): AuthResult<String>
    suspend fun notifyServer(code: String?, state: String?): AuthResult<User>

}