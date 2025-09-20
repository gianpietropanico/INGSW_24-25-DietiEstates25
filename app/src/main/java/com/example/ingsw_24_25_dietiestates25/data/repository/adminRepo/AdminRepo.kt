package com.example.ingsw_24_25_dietiestates25.data.repository.adminRepo

import com.example.ingsw_24_25_dietiestates25.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.model.result.ApiResult

interface AdminRepo {
    suspend fun getAllAgencies(): ApiResult<List<Agency>>
    suspend fun getAllSuppAdmins(): ApiResult<List<User>>
    suspend fun decideRequest(adminEmail: String, agencyEmail: String, typeRequest: String): ApiResult<Unit>
    suspend fun addSuppAdmin(adminEmail: String , adminId: String , recipientEmail: String, username: String): ApiResult<Unit>

}