package com.example.ingsw_24_25_dietiestates25.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import java.time.LocalDate

class WeatherApiClient {

    val daysAhead: Long = 7
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getWeatherData(lat: Double, long: Double, date: LocalDate): WeatherDto {
        val endDate = date.plusDays(daysAhead)
        return client.get("https://api.open-meteo.com/v1/forecast") {
            parameter("latitude", lat)
            parameter("longitude", long)
            parameter(
                "hourly",
                "temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl"
            )
            parameter("start_date", date.toString())
            parameter("end_date", date.toString())
        }.body()
    }
}