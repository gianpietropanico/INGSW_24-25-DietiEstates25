package com.example.ingsw_24_25_dietiestates25.data.api.notificationApi

import com.example.ingsw_24_25_dietiestates25.model.dataclass.Notification
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse

class NotificationApiImpl(
    private val httpClient: HttpClient
): NotificationApi {
    override suspend fun getNotifications(userEmail: String): List<Notification> {
        return httpClient.get("http://10.0.2.2:8080/notifications/$userEmail").body()
    }

    override suspend fun markAsRead(notificationId: String): Boolean {
        val response: HttpResponse = httpClient.post("http://10.0.2.2:8080/notifications/mark-read/$notificationId")
        return response.status.value == 200
    }
}