package com.example.ingsw_24_25_dietiestates25.data.repository.notificationRepo

import com.example.ingsw_24_25_dietiestates25.model.dataclass.Notification

interface NotificationRepository {
    suspend fun fetchNotifications(userEmail: String): List<Notification>
    suspend fun markNotificationAsRead(notificationId: String): Boolean
}