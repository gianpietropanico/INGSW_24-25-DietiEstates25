package com.example.ingsw_24_25_dietiestates25.data.repository.profileRepo

import android.util.Log
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.data.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.UserInfoRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class ProfileRepoImp @Inject constructor(
    private val httpClient: HttpClient,
    private val sessionManager: UserSessionManager
) :ProfileRepo {

    private val baseURL = "http://10.0.2.2:8080"
    private val userSessionManager = sessionManager

    override suspend fun logout() {
        try {
            userSessionManager.clear()
            Log.d("AuthRepository", "Logout completato con successo")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Errore durante il logout", e)
        }
    }

    override suspend fun updateUserInfo(value: String, type: String): ApiResult<Unit> {
        return try {
            Log.d("updateUserInfo", "tipo richiesta: $type")

            // Validazione per nome e cognome
            if (type == "Name And Surname") {
                val clean = value.trim().replace("\\s+".toRegex(), " ")
                val parts = clean.split(" ", limit = 2)
                Log.d("updateUserInfo", "Validazione full name: $clean")

                if (parts.size < 2) {
                    return ApiResult.Unauthorized("Inserisci sia nome che cognome")
                }
            }

            val email = userSessionManager.currentUser.value?.email
                ?: return ApiResult.Unauthorized("Utente non loggato")

            val request = UserInfoRequest(
                email = email,
                value = value,
                typeRequest = type
            )

            val response = httpClient.post("$baseURL/user/profile/user-info") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    updateSessionManagerInfo(value, type)
                    ApiResult.Success(Unit, "Operation Successfull, Info updated")
                }
                HttpStatusCode.Conflict -> {
                    val err = response.bodyAsText()
                    ApiResult.Unauthorized("Update failed: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Update failed")
                else -> ApiResult.UnknownError("Errore HTTP ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }



    override suspend fun resetPassword(oldPassword: String, newPassword: String): ApiResult<Unit> {
        return try {
            val email = userSessionManager.currentUser.value?.email
                ?: return ApiResult.Unauthorized("Utente non loggato")

            val request = AuthRequest(
                email = email,
                password = oldPassword,
                newPassword = newPassword
            )

            val response = httpClient.post("$baseURL/user/profile/reset-password") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK -> ApiResult.Success(Unit, "Operation Successfull, Password changed")
                HttpStatusCode.Conflict -> {
                    val err = response.bodyAsText()
                    ApiResult.Unauthorized("Reset failed: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Reset failed")
                else -> ApiResult.UnknownError("Errore HTTP ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }


    private fun updateSessionManagerInfo(value : String , type: String) {
        when (type.lowercase()) {
            "username" -> userSessionManager.currentUser.value!!.username = value
            "name and surname" ->{
                val parts = value.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
                userSessionManager.currentUser.value!!.name = parts.getOrNull(0) ?: ""
                userSessionManager.currentUser.value!!.surname = parts.getOrNull(1) ?: ""
            }
        }
    }
}