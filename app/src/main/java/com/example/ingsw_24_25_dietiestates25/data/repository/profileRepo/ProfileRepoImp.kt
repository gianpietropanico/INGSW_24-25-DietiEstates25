package com.example.ingsw_24_25_dietiestates25.data.repository.profileRepo

import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.api.authApi.AuthApi
import com.example.ingsw_24_25_dietiestates25.data.api.profileApi.ProfileApi
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.request.UserInfoRequest
import com.example.ingsw_24_25_dietiestates25.model.result.AuthResult
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class ProfileRepoImp @Inject constructor(
    private val sessionManager: UserSessionManager,
    private val profileApi: ProfileApi,
) :ProfileRepo {

    override suspend fun logout() {
        try {
            sessionManager.clear()
            Log.d("AuthRepository", "Logout completato con successo")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Errore durante il logout", e)
        }
    }

    override suspend fun updateUserInfo( value: String, type: String): AuthResult<Unit> {
        return try {

            if (type == "Full Name") {
                val parts = value.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }

                if (parts.size < 2) {
                    return AuthResult.Unauthorized("Inserisci almeno nome e cognome")
                }
            }

            profileApi.updateUserInfo(UserInfoRequest(email = sessionManager.currentUser.value!!.email, value = value, typeRequest = type))

            updateSessionManagerInfo(value, type)

            AuthResult.Success(Unit, "Operation Successfull, Info updated")

        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> AuthResult.Unauthorized("Update failed ")
                else -> AuthResult.UnknownError("Errore")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Errore generico")
        }
    }


    override suspend fun resetPassword( oldPassword: String, newPassword: String): AuthResult<Unit> {
        return try {
            profileApi.resetPassword(AuthRequest(email = sessionManager.currentUser.value!!.email, password = oldPassword,  newPassword = newPassword))
            AuthResult.Success(Unit, "Operation Successfull, Password changed")
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> AuthResult.Unauthorized("Reset failed ")
                else -> AuthResult.UnknownError("Errore")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Errore generico")
        }
    }

    private fun updateSessionManagerInfo(value : String , type: String) {
        when (type.lowercase()) {
            "username" -> sessionManager.currentUser.value!!.username = value
            "name" -> sessionManager.currentUser.value!!.name = value
            "surname" -> sessionManager.currentUser.value!!.surname = value
        }
    }
}