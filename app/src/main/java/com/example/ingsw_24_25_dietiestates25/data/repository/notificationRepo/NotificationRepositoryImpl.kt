package com.example.ingsw_24_25_dietiestates25.data.repository.notificationRepo

import com.example.ingsw_24_25_dietiestates25.data.api.notificationApi.NotificationApi
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Notification
import io.ktor.client.HttpClient
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor (
    private val api: NotificationApi
): NotificationRepository {
    override suspend fun fetchNotifications(userEmail: String): List<Notification> {
        return api.getNotifications(userEmail)
    }

    override suspend fun markNotificationAsRead(notificationId: String): Boolean {
        return api.markAsRead(notificationId)

    }
}