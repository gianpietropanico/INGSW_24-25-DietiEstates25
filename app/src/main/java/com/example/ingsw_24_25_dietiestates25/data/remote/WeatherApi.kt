package com.example.ingsw_24_25_dietiestates25.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.time.LocalDate
import javax.inject.Inject

class WeatherApiClient @Inject constructor(){

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }
    

//    suspend fun getWeatherData(lat: Double, long: Double, date: LocalDate): WeatherDto {
//
//        return client.get("https://api.open-meteo.com/v1/forecast") {
//            parameter("latitude", lat)
//            parameter("longitude", long)
//            parameter(
//                "hourly",
//                "temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl"
//            )
//            parameter("start_date", date.toString())
//            parameter("end_date", date.plusDays(daysAhead).toString())
//        }.body()
//    }


    private val maxDaysAhead: Long = 7

    suspend fun getWeatherData(lat: Double, long: Double, date: LocalDate): WeatherDto = withContext(
        Dispatchers.IO) {
        try {
            // Controlla la data: massimo 7 giorni da oggi
            val startDate = if (date.isBefore(LocalDate.now())) LocalDate.now() else date
            val endDate = if (date.plusDays(maxDaysAhead).isAfter(LocalDate.now().plusDays(maxDaysAhead))) {
                LocalDate.now().plusDays(maxDaysAhead)
            } else date.plusDays(maxDaysAhead)

            client.get("https://api.open-meteo.com/v1/forecast") {
                parameter("latitude", lat)
                parameter("longitude", long)
                parameter(
                    "hourly",
                    "temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl"
                )
                parameter("start_date", startDate.toString())
                parameter("end_date", endDate.toString())
            }.body()
        } catch (e: Exception) {
            // Log per debug
            println("WeatherApiClient fallback: ${e.message}")

            // Ritorna dati di fallback con messaggio
            WeatherDto(
                weatherData = WeatherDataDto(
                    time = listOf(date.toString()),
                    temperatures = listOf(20.0),
                    weatherCodes = listOf(0),
                    humidities = listOf(50.0),
                    windSpeeds = listOf(5.0),
                    pressures = listOf(1013.0)
                ),
                isFallback = true,
                message = "Weather data not available. Showing estimated values."
            )
        }
    }


}