package com.example.ingsw_24_25_dietiestates25.data.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object NetworkClient {
    val httpClient: HttpClient = HttpClient(CIO) {
        // Configura la serializzazione JSON
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Ignora campi JSON non definiti nel modello
                isLenient = true // Accetta JSON meno rigidi
            })
        }

        // Configura il logging per il debug
        install(Logging) {
            logger = Logger.SIMPLE // Logger semplice
            level = LogLevel.BODY // Livello di dettaglio: corpo delle richieste e risposte
        }
    }
}