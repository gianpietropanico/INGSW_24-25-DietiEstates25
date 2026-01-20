package com.example.ingsw_24_25_dietiestates25.data.repository.adminRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.request.AdminRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.UserInfoRequest
import com.example.ingsw_24_25_dietiestates25.data.model.response.ListResponse
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class AdminRepoImp  @Inject constructor(
    private val httpClient: HttpClient
) : AdminRepo {

    private val baseURL = "http://84.8.252.211:8080/"

    override suspend fun getAllAgencies(): ApiResult<List<Agency>> {
        return try {
            val response = httpClient.get("$baseURL/agency/agencies") {
                accept(ContentType.Application.Json)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ListResponse<List<Agency>> = response.body()
                    if (body.success && body.data != null) {
                        ApiResult.Success(body.data)
                    } else {
                        ApiResult.UnknownError(body.message ?: "Unknown Error")
                    }
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status}: $err")
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

    override suspend fun getAllSuppAdmins(): ApiResult<List<User>> {
        return try {
            val request = UserInfoRequest("sysadmin@system.com", typeRequest = "super_admin", value = "SUPPORT_ADMIN")

            val response = httpClient.get("$baseURL/admin/users") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ListResponse<List<User>> = response.body()
                    if (body.success && body.data != null) {
                        ApiResult.Success(body.data)
                    } else {
                        ApiResult.UnknownError(body.message ?: "Unknown Error")
                    }
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status}: $err")
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


    override suspend fun addSuppAdmin(admin: User, recipientEmail: String, userEmail: String): ApiResult<Unit> {
        if (recipientEmail.isEmpty() || userEmail.isEmpty()) {
            return ApiResult.UnknownError("You must fill in all the fields")
        }

        return try {
            val request = AdminRequest(
                adminEmail = admin.email,
                adminId = admin.id,
                suppAdminEmail = recipientEmail,
                email = userEmail
            )

            val response = httpClient.post("$baseURL/admin/users") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return if (response.status.isSuccess()) {
                val msg = response.bodyAsText()
                ApiResult.Success(Unit, msg.ifEmpty { "Operation completed" })
            } else {
                val err = response.bodyAsText()
                ApiResult.UnknownError("HTTP Error ${response.status}: $err")
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


    override suspend fun decideRequest(adminEmail: String, agencyEmail: String, typeRequest: String): ApiResult<Unit> {
        return try {
            val request = UserInfoRequest(
                adminEmail,
                typeRequest = typeRequest,
                value = agencyEmail
            )

            val response = httpClient.post("$baseURL/agency/request-decision") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }


            return when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ListResponse<Unit> = response.body()
                    if (body.success) {
                        ApiResult.Success(Unit, body.message ?: "Operation completed")
                    } else {
                        ApiResult.UnknownError(body.message ?: "Unknown Error")
                    }
                }
                else -> {
                    val err = response.bodyAsText()
                    ApiResult.UnknownError("HTTP Error ${response.status}: $err")
                }
            }

        } catch (e: Exception) {
            ApiResult.UnknownError("Generic Error: ${e.localizedMessage}")
        }
    }



}