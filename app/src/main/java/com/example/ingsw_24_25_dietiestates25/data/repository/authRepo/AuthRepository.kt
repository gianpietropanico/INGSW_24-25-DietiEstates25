package com.example.ingsw_24_25_dietiestates25.data.repository.authRepo

import com.example.ingsw_24_25_dietiestates25.model.result.AuthResult

interface AuthRepository {
    suspend fun getLoggedUser() : AuthResult<Unit>
    suspend fun signUp(email: String, password: String, profilePicBase64 : String): AuthResult<Unit>
    suspend fun signIn(email:String, password: String): AuthResult<Unit>
    suspend fun authWithThirdParty(email: String, username : String): AuthResult<Unit>
    suspend fun sendAgencyRequest(email: String, password: String, agencyName: String): AuthResult<Unit>

    //GITHUB
    suspend fun fetchState(): AuthResult<String>
    suspend fun exchangeGitHubCode(code: String?, state: String?): AuthResult<Unit>



}