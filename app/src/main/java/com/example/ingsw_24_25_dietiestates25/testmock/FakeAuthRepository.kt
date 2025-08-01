package com.example.ingsw_24_25_dietiestates25.testmock

import com.example.ingsw_24_25_dietiestates25.data.repository.AuthRepository
import com.example.ingsw_24_25_dietiestates25.model.result.AuthResult

class FakeAuthRepository : AuthRepository {
    override suspend fun getLoggedUser(): AuthResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(email: String, password: String, profilePicBase64: String): AuthResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(email: String, password: String): AuthResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): AuthResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun authWithThirdParty(email: String, username: String): AuthResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun sendAgencyRequest(
        email: String,
        password: String,
        agencyName: String
    ): AuthResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchState(): AuthResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun exchangeGitHubCode(code: String?, state: String?): AuthResult<Unit> {
        TODO("Not yet implemented")
    }
}