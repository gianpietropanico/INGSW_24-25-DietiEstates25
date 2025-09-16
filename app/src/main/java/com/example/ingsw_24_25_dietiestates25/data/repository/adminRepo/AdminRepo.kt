package com.example.ingsw_24_25_dietiestates25.data.repository.adminRepo

import com.example.ingsw_24_25_dietiestates25.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.model.result.ApiResult

interface AdminRepo {
    suspend fun getAllAgencies(): ApiResult<List<Agency>>
    suspend fun decideRequest(adminEmail: String, agencyEmail: String, typeRequest: String): ApiResult<Unit>

}