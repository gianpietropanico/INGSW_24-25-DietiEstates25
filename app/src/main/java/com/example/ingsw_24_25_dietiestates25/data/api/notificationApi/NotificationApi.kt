package com.example.ingsw_24_25_dietiestates25.data.api.notificationApi

import com.example.ingsw_24_25_dietiestates25.model.dataclass.Notification

interface NotificationApi {
    suspend fun getNotifications(userEmail: String): List<Notification>
    suspend fun markAsRead(notificationId: String): Boolean
}