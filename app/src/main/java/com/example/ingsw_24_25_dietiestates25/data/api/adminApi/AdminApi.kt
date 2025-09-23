package com.example.ingsw_24_25_dietiestates25.data.api.adminApi

import com.example.ingsw_24_25_dietiestates25.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.model.request.AdminRequest
import com.example.ingsw_24_25_dietiestates25.model.request.UserInfoRequest
import com.example.ingsw_24_25_dietiestates25.model.response.ListResponse
import com.example.ingsw_24_25_dietiestates25.model.result.ApiResult


interface AdminApi {
    suspend fun getAllSuppAdmins(request: UserInfoRequest): ListResponse<List<User>>
    suspend fun getAllAgencies(): ListResponse<List<Agency>>
    suspend fun decideRequest(request: UserInfoRequest): ListResponse<Unit>
    suspend fun addSuppAdmin(request: AdminRequest): ApiResult<String>
}
