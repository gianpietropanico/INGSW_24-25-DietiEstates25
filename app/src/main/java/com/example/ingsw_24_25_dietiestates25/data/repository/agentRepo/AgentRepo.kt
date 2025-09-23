package com.example.ingsw_24_25_dietiestates25.data.repository.agentRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult

interface AgentRepo {
    suspend fun getAllAgent( agencyEmail: String ) : ApiResult<List<User>>
    suspend fun updateAgencyName ( agencyEmail : String , agencyName: String) : ApiResult<Unit>
    suspend fun updateAgencyPic( agencyId: String , profilePic : String ) : ApiResult<Unit>
    suspend fun addUserBySendingEmail(user : User, recipientEmail: String, userEmail: String): ApiResult<Unit>
    suspend fun addAgent(agencyEmail: String , agentEmail: String): ApiResult<Unit>
    suspend fun getUserIdByEmail(email: String): ApiResult<String>

}