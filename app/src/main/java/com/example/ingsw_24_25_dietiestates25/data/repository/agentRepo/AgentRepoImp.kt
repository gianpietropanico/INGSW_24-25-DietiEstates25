package com.example.ingsw_24_25_dietiestates25.data.repository.agentRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.request.AdminRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.UserInfoRequest
import com.example.ingsw_24_25_dietiestates25.data.model.response.ListResponse
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.accept
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.isSuccess


class AgentRepoImp @Inject constructor(
    private val httpClient: HttpClient,
) :AgentRepo {

    private val baseURL = "http://10.0.2.2:8080"

    override suspend fun getAllAgent(agencyEmail: String): ApiResult<List<User>> {
        return try {
            val response = httpClient.get("$baseURL/admin/users") {
                contentType(ContentType.Application.Json)
                setBody(UserInfoRequest(agencyEmail, typeRequest = "agent_user", value = ""))
            }

            if (response.status == HttpStatusCode.OK) {
                val body: ListResponse<List<User>> = response.body()
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
            ApiResult.UnknownError("Generic Error: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserIdByEmail(email: String): ApiResult<String> {
        return try {
            val response = httpClient.get("$baseURL/admin/users/by-email") {
                url {
                    parameters.append("email", email)
                }
                accept(ContentType.Application.Json)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val body: Map<String, String> = response.body()
                    val userId = body["id"]

                    if (userId != null) {
                        ApiResult.Success(userId, "ID successfully retrieved")
                    } else {
                        ApiResult.UnknownError("Server response without 'id' field")
                    }
                }
                HttpStatusCode.NotFound -> {
                    ApiResult.UnknownError("No user found for the email $email")
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Forbidden -> ApiResult.Unauthorized("Access denied")
                else -> ApiResult.UnknownError("HTTP Error: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic Error: ${e.localizedMessage}")
        }
    }

    override suspend fun addUserBySendingEmail(user : User, recipientEmail: String, userEmail: String): ApiResult<Unit> {

        if (recipientEmail.isBlank() || userEmail.isBlank()) {
            return ApiResult.UnknownError("You must fill in all the fields")
        }

        return try {
            val request = AdminRequest(
                adminEmail = user.email,
                adminId = user.id,
                suppAdminEmail = recipientEmail,
                email = userEmail
            )

            val response = httpClient.post("$baseURL/admin/users") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                ApiResult.Success(Unit, response.bodyAsText())
            } else {
                val err = response.bodyAsText()
                ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
            }

        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Forbidden -> ApiResult.Unauthorized("Access denied")
                else -> ApiResult.UnknownError("HTTP Error: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic Error: ${e.localizedMessage}")
        }
    }

    override suspend fun addAgent(agencyEmail: String , agentEmail: String): ApiResult<Unit> {

        return try {
            val request = UserInfoRequest(
                email = agentEmail,
                value = agencyEmail,
                typeRequest = ""
            )

            val response = httpClient.post("$baseURL/agency/agent") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                ApiResult.Success(Unit, response.bodyAsText())
            } else {
                val err = response.bodyAsText()
                ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
            }

        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Forbidden -> ApiResult.Unauthorized("Access denied")
                else -> ApiResult.UnknownError("HTTP Error: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic Error: ${e.localizedMessage}")
        }
    }

    override suspend fun updateAgencyName(agencyEmail: String, agencyName: String): ApiResult<Unit> {
        return try {
            val response = httpClient.put("$baseURL/agency/name") {
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "agencyEmail" to agencyEmail,
                        "agencyName" to agencyName
                    )
                )
            }

            if (response.status.isSuccess()) {
                ApiResult.Success(Unit, "Agency name updated successfully")
            } else {
                val err = response.bodyAsText()
                ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Network error: ${e.localizedMessage}")
        }

    }

    override suspend fun updateAgencyPic(agencyId: String, profilePic: String): ApiResult<Unit> {
        return try {
            val response = httpClient.put("$baseURL/agency/pic") {
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "agencyId" to agencyId,
                        "profilePic" to profilePic
                    )
                )
            }

            if (response.status.isSuccess()) {
                ApiResult.Success(Unit, "Profile picture updated successfully")
            } else {
                val err = response.bodyAsText()
                ApiResult.UnknownError("HTTP Error ${response.status.value}: $err")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Network error: ${e.localizedMessage}")
        }
    }
}


