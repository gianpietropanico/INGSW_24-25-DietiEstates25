package com.example.ingsw_24_25_dietiestates25.data.jwt

import android.util.Base64
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class JwtPayload(
    @SerialName("userId") val userId: String,
    val aud: String? = null,
    val iss: String? = null,
    val exp: Long?   = null,
    val username: String? = null
)

fun parseJwtPayload(jwt: String): JwtPayload {
    val parts = jwt.split(".")
    require(parts.size == 3) { "Token non valido" }
    val decoded = Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    val json = String(decoded, Charsets.UTF_8)
    return Json { ignoreUnknownKeys = true }.decodeFromString(json)
}