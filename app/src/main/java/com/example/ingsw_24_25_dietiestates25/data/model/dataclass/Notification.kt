package com.example.ingsw_24_25_dietiestates25.data.model.dataclass

data class Notification(
    val id: String,
    val recipientEmail: String,
    val message: String,
    val isRead: Boolean
)
