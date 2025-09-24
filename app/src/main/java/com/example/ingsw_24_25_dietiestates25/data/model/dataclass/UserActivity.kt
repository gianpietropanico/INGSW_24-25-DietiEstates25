package com.example.ingsw_24_25_dietiestates25.data.model.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserActivity(
    val id: String,
    val userId: String,
    val type: ActivityType,
    val text: String,
    val date: String,
)

@Serializable
enum class ActivityType(val label: String) {

    @SerialName("INSERT")
    INSERT("INSERT"),

    @SerialName("ACCEPTED")
    ACCEPTED("ACCEPTED"),

    @SerialName("DECLINED")
    DECLINED("DECLINED"),

    @SerialName("VIEWED")
    VIEWED("VIEWED"),

    @SerialName("OFFERED")
    OFFERED("OFFERED"),

    @SerialName("BOOKED")
    BOOKED("BOOKED")
}