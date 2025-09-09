package com.example.ingsw_24_25_dietiestates25.data.api.profileApi

import com.example.ingsw_24_25_dietiestates25.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.request.UserInfoRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class ProfileApiImp @Inject constructor (
    private val httpClient: HttpClient
) : ProfileApi{

    override suspend fun resetPassword(request: AuthRequest) {
        httpClient.post("http://10.0.2.2:8080/user/profile/reset-password") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    override suspend fun updateUserInfo(request: UserInfoRequest) {
        httpClient.post("http://10.0.2.2:8080/user/profile/user-info") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

}