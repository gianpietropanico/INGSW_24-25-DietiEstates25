package com.example.ingsw_24_25_dietiestates25.data.repository.weatherRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.WeatherInfo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import java.time.LocalDate
import javax.inject.Inject
import io.ktor.client.statement.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class WeatherRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : WeatherRepository {

    override suspend fun getWeatherForDate(
        listing: PropertyListing,
        date: LocalDate
    ): List<WeatherInfo> {
        val lat = listing.property.latitude
        val lon = listing.property.longitude

        val response: HttpResponse = httpClient.get("https://api.open-meteo.com/v1/forecast") {
            url {
                parameters.append("latitude", lat.toString())
                parameters.append("longitude", lon.toString())
                parameters.append("hourly", "temperature_2m,weathercode")
                parameters.append("timezone", "auto")
                parameters.append("start_date", date.toString())
                parameters.append("end_date", date.toString())
            }
        }

        val body = response.bodyAsText()
        val parsed = Json { ignoreUnknownKeys = true }.decodeFromString<OpenMeteoResponse>(body)

        val times = parsed.hourly.time
        val temps = parsed.hourly.temperature
        val codes = parsed.hourly.weathercode

        return times.zip(temps.zip(codes)) { t, (temp, code) ->
            WeatherInfo(
                time = t.substringAfter("T"), // es. "2025-10-07T09:00" -> "09:00"
                temperature = temp.toString(),
                condition = weatherCodeToText(code)
            )
        }
    }

    private fun weatherCodeToText(code: Int): String = when (code) {
        0 -> "Soleggiato"
        1, 2, 3 -> "Parzialmente nuvoloso"
        45, 48 -> "Nebbia"
        51, 53, 55 -> "Pioviggine"
        61, 63, 65 -> "Pioggia"
        71, 73, 75 -> "Neve"
        95 -> "Temporale"
        else -> "N/D"
    }
}

@Serializable
data class OpenMeteoResponse(
    val hourly: HourlyData
)

@Serializable
data class HourlyData(
    val time: List<String>,
    @SerialName("temperature_2m") val temperature: List<Float>,
    val weathercode: List<Int>
)