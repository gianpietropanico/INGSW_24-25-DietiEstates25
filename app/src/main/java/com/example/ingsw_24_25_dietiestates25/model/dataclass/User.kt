package com.example.ingsw_24_25_dietiestates25.model.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    var username: String,
    var name: String? = null,
    var surname: String? = null,
    val email: String,
    val role: Role,
    val profilePicture: String? = null
)

@Serializable
enum class Role {
    @SerialName("SUPER_ADMIN")
    SUPER_ADMIN,

    @SerialName("SUPPORT_ADMIN")
    SUPPORT_ADMIN,

    @SerialName("AGENCY_ADMIN")
    AGENCY_ADMIN,

    @SerialName("AGENT_USER")
    AGENT_USER,

    @SerialName("LOCAL_USER")
    LOCAL_USER,

    @SerialName("THIRDPARTY_USER")
    THIRDPARTY_USER,

    @SerialName("PENDING_AGENCY_ADMIN")
    PENDING_AGENCY_ADMIN
}