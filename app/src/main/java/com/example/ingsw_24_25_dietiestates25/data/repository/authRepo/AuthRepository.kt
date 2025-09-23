package com.example.ingsw_24_25_dietiestates25.data.repository.authRepo

import com.example.ingsw_24_25_dietiestates25.model.dataclass.AgencyUser
import com.example.ingsw_24_25_dietiestates25.model.result.ApiResult


interface AuthRepository {
    suspend fun getLoggedUser() : ApiResult<Unit>
    suspend fun signUp(email: String, password: String): ApiResult<Unit>
    suspend fun signIn(email:String, password: String): ApiResult<Unit>
    suspend fun authWithThirdParty(email: String, username : String): ApiResult<Unit>
    suspend fun sendAgencyRequest(email: String, password: String, agencyName: String): ApiResult<AgencyUser>
    suspend fun setUpAgency( userId : String ) : ApiResult<Unit>

    //GITHUB
    suspend fun fetchState(): ApiResult<String>
    suspend fun githubOauth(code: String?, state: String?): ApiResult<Unit>



}