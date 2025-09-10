package com.example.ingsw_24_25_dietiestates25.model.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    var username: String,
    var name: String? = null,
    var surname: String? = null,
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
