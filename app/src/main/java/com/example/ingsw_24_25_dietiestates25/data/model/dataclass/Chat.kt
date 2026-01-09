package com.example.ingsw_24_25_dietiestates25.data.model.dataclass


import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: String,
    val listing: PropertyListing,
    val buyerUser: User,
    val agentUser: User,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis(),

    // Offerte (nullable - può non esistere)
    val offer: Offer? = null,

    // Appuntamenti (lista - possono essere multipli)
    val appointments: List<Appointment> = emptyList()
)
