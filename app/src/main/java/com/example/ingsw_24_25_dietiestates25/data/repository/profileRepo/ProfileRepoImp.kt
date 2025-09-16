package com.example.ingsw_24_25_dietiestates25.data.repository.profileRepo

import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.api.authApi.AuthApi
import com.example.ingsw_24_25_dietiestates25.data.api.profileApi.ProfileApi
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.request.UserInfoRequest
import com.example.ingsw_24_25_dietiestates25.model.result.ApiResult
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

    override suspend fun updateUserInfo( value: String, type: String): ApiResult<Unit> {
        return try {
            Log.d("updateUserInfo", "typo richiesta : $type")

            if (type == "Name And Surname" ) {
                val clean = value.trim().replace("\\s+".toRegex(), " ")
                val parts = clean.split(" ", limit = 2)
                Log.d("updateUserInfo", "Validazione full name: $clean")

                if (parts.size < 2) {
                    return ApiResult.Unauthorized("Inserisci sia nome che cognome")
                }
            }

            profileApi.updateUserInfo(UserInfoRequest(email = sessionManager.currentUser.value!!.email, value = value, typeRequest = type))

            updateSessionManagerInfo(value, type)

            ApiResult.Success(Unit, "Operation Successfull, Info updated")

        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Update failed ")
                else -> ApiResult.UnknownError("Errore")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico")
        }
    }


    override suspend fun resetPassword( oldPassword: String, newPassword: String): ApiResult<Unit> {
        return try {
            profileApi.resetPassword(AuthRequest(email = sessionManager.currentUser.value!!.email, password = oldPassword,  newPassword = newPassword))
            ApiResult.Success(Unit, "Operation Successfull, Password changed")
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Reset failed ")
                else -> ApiResult.UnknownError("Errore")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico")
        }
    }

    private fun updateSessionManagerInfo(value : String , type: String) {
        when (type.lowercase()) {
            "username" -> sessionManager.currentUser.value!!.username = value
            "name and surname" ->{
                val parts = value.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
                sessionManager.currentUser.value!!.name = parts.getOrNull(0) ?: ""
                sessionManager.currentUser.value!!.surname = parts.getOrNull(1) ?: ""
            }
        }
    }
}