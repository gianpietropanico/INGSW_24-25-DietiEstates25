package com.example.ingsw_24_25_dietiestates25.data.auth

interface AuthRepository {
    suspend fun signUp(email: String, password: String):AuthResult<Unit>
    suspend fun signIn(email:String, password: String):AuthResult<Unit>
    suspend fun authenticate():AuthResult<Unit>
}