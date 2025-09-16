package com.example.ingsw_24_25_dietiestates25.data.api.adminApi

import com.example.ingsw_24_25_dietiestates25.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.model.request.UserInfoRequest
import com.example.ingsw_24_25_dietiestates25.model.response.ListResponse

interface AdminApi {

    suspend fun getAllAgencies(): ListResponse<List<Agency>>
    suspend fun decideRequest(request: UserInfoRequest): ListResponse<Unit>

}
