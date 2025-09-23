package com.example.ingsw_24_25_dietiestates25.model.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class AgencyUser(
    val userId: String, //id dell'utente
    val agencyId: String, //id della agenzia
)