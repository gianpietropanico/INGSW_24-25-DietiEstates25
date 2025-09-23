package com.example.ingsw_24_25_dietiestates25.data.repository.adminRepo

import com.example.ingsw_24_25_dietiestates25.data.api.adminApi.AdminApi

import com.example.ingsw_24_25_dietiestates25.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.model.request.AdminRequest
import com.example.ingsw_24_25_dietiestates25.model.request.UserInfoRequest

import com.example.ingsw_24_25_dietiestates25.model.result.ApiResult
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class AdminRepoImp  @Inject constructor(
    private val adminApi: AdminApi
) : AdminRepo {

    override suspend fun getAllAgencies(): ApiResult<List<Agency>> {
        return try {
            val response = adminApi.getAllAgencies()

            if (response.success && response.data != null) {
                ApiResult.Success(response.data)
            } else {
                ApiResult.UnknownError(response.message ?: "Errore sconosciuto")
            }

        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Forbidden -> ApiResult.Unauthorized("Accesso negato")
                else -> ApiResult.UnknownError("Errore HTTP: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }
    override suspend fun getAllSuppAdmins(): ApiResult<List<User>> {
        return try {
            val response = adminApi.getAllSuppAdmins(UserInfoRequest("sysadmin@system.com", typeRequest = "super_admin", value = "SUPPORT_ADMIN"))

            if (response.success && response.data != null) {
                ApiResult.Success(response.data)
            } else {
                ApiResult.UnknownError(response.message ?: "Errore sconosciuto")
            }

        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Forbidden -> ApiResult.Unauthorized("Accesso negato")
                else -> ApiResult.UnknownError("Errore HTTP: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }


    override suspend fun addSuppAdmin(admin : User , recipientEmail: String, username: String): ApiResult<Unit> {

        if ( recipientEmail.isEmpty() || username.isEmpty() )  return ApiResult.UnknownError("Devi compilare tutti i campi")

        return try {

            val response = adminApi.addSuppAdmin(
                AdminRequest(
                    adminEmail = admin.email,
                    adminId = admin.id,
                    suppAdminEmail = recipientEmail,
                    usernameSuppAdmin = username,
                    emailDomain = "@system.com"
                )
            )

            if (response is ApiResult.Success) {
                ApiResult.Success(Unit, response.message ?: "Operazione completata")
            } else {
                ApiResult.UnknownError(response.message ?: "Errore sconosciuto")
            }

        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Forbidden -> ApiResult.Unauthorized("Accesso negato")
                else -> ApiResult.UnknownError("Errore HTTP: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun decideRequest(adminEmail: String, agencyEmail: String, typeRequest: String): ApiResult<Unit> {
        return try {
            val response = adminApi.decideRequest(UserInfoRequest(adminEmail, typeRequest = typeRequest, value = agencyEmail))

            if (response.success) {
                ApiResult.Success(Unit, response.message ?: "Operazione completata")
            } else {
                ApiResult.UnknownError(response.message ?: "Errore sconosciuto")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }


}