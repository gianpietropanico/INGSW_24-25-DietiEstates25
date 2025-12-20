package com.example.ingsw_24_25_dietiestates25.data.repository.profileRepo

import android.app.Activity
import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.UserActivity
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.data.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.UserInfoRequest
import com.example.ingsw_24_25_dietiestates25.data.model.response.ListResponse
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class ProfileRepoImp @Inject constructor(
    private val httpClient: HttpClient,
    private val sessionManager: UserSessionManager
) : ProfileRepo {

    private val baseURL = "http://10.0.2.2:8080"
    private val userSessionManager = sessionManager

    override suspend fun logout() {
        try {
            userSessionManager.clear()
            Log.d("AuthRepository", "Logout completed successfully")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error during logout", e)
        }
    }

    override suspend fun updateUserInfo(value: String, type: String): ApiResult<Unit> {
        return try {
            Log.d("updateUserInfo", "Request type: $type")

            // Name and surname validation
            if (type == "Name And Surname") {
                val clean = value.trim().replace("\\s+".toRegex(), " ")
                val parts = clean.split(" ", limit = 2)
                Log.d("updateUserInfo", "Full name validation: $clean")

                if (parts.size < 2) {
                    return ApiResult.Unauthorized("Please enter both name and surname")
                }
            }

            val email = userSessionManager.currentUser.value?.email
                ?: return ApiResult.Unauthorized("User not logged in")

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
                    ApiResult.Success(Unit, "Operation Successful, Info updated")
                }
                HttpStatusCode.Conflict -> {
                    val err = response.bodyAsText()
                    ApiResult.Unauthorized("Update failed: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Update failed")
                else -> ApiResult.UnknownError("HTTP Error ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

    override suspend fun getActivities(id: String): ApiResult<List<UserActivity>> {
        return try {
            val response = httpClient.get("$baseURL/user/profile/activities") {
                url {
                    parameters.append("userId", id)
                }
                accept(ContentType.Application.Json)
            }

            if (response.status == HttpStatusCode.OK) {
                val body: ListResponse<List<UserActivity>> = response.body()
                if (body.success && body.data != null) {
                    ApiResult.Success(body.data)
                } else {
                    ApiResult.UnknownError(body.message ?: "Unknown server error")
                }
            } else {
                ApiResult.UnknownError("HTTP Error: ${response.status.value}")
            }

        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Forbidden -> ApiResult.Unauthorized("Access denied")
                else -> ApiResult.UnknownError("HTTP Error: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

    override suspend fun resetPassword(oldPassword: String, newPassword: String): ApiResult<Unit> {
        return try {
            val email = userSessionManager.currentUser.value?.email
                ?: return ApiResult.Unauthorized("User not logged in")

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
                HttpStatusCode.OK -> ApiResult.Success(Unit, "Operation Successful, Password changed")
                HttpStatusCode.Conflict -> {
                    val err = response.bodyAsText()
                    ApiResult.Unauthorized("Reset failed: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Reset failed")
                else -> ApiResult.UnknownError("HTTP Error ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

    private fun updateSessionManagerInfo(value: String, type: String) {
        when (type.lowercase()) {
            "username" -> userSessionManager.currentUser.value!!.username = value
            "name and surname" -> {
                val parts = value.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
                userSessionManager.currentUser.value!!.name = parts.getOrNull(0) ?: ""
                userSessionManager.currentUser.value!!.surname = parts.getOrNull(1) ?: ""
            }
        }
    }
}