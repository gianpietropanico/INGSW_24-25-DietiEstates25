package com.example.ingsw_24_25_dietiestates25.data.model.dataclass

import com.example.ingsw_24_25_dietiestates25.ui.utils.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class WeatherInfo(
    val time: String,       // "09:00"
    val condition: String,  // "Soleggiato"
    val temperature: String,// "22°C"
    val iconUrl: String? = null // opzionale, per icone vere
)

@Serializable
data class DailyWeather(
    @Serializable(with = LocalDateSerializer::class)

    val date: LocalDate,
    val condition: String // es: "sole", "pioggia", "nuvoloso"
)
