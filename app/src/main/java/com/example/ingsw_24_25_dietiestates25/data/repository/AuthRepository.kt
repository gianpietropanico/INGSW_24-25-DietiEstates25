package com.example.ingsw_24_25_dietiestates25.data.repository

import com.example.ingsw_24_25_dietiestates25.model.result.AuthResult

interface AuthRepository {
    suspend fun signUp(email: String, password: String): AuthResult<Unit>
    suspend fun signIn(email:String, password: String): AuthResult<Unit>
    suspend fun resetPassword(email: String, oldPassword: String , newPassword: String): AuthResult<Unit>
    suspend fun authWithThirdParty(email: String, username : String): AuthResult<Unit>
    suspend fun logout()

    //GITHUB
    suspend fun fetchState(): AuthResult<String>
    suspend fun exchangeGitHubCode(code: String?, state: String?): AuthResult<Unit>



}