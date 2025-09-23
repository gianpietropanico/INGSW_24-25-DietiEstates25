package com.example.ingsw_24_25_dietiestates25.data.repository.adminRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult

interface AdminRepo {
    suspend fun getAllAgencies(): ApiResult<List<Agency>>
    suspend fun getAllSuppAdmins(): ApiResult<List<User>>
    suspend fun decideRequest(adminEmail: String, agencyEmail: String, typeRequest: String): ApiResult<Unit>
    suspend fun addSuppAdmin(admin : User, recipientEmail: String, userEmail: String): ApiResult<Unit>
}