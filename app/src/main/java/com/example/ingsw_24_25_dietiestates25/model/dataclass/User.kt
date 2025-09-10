package com.example.ingsw_24_25_dietiestates25.model.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String,
    val name: String? = null,
    val surname: String? = null,
    val email: String,
    val type: String,   // "superadmin" "agency_admin" "agent" "localuser" "thirdpartyuser"
    val profilePicture: String? = null
)

//da trasformare tipo in enumerazione
enum class Role(val label: String) {
    A("SUPERADMIN"),
    B("AGENCY_ADMIN"),
    C("AGENT"),
    D("LOCALUSER"),
    E("THIRDPARTYUSER")
}
