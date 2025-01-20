package com.example.ingsw_24_25_dietiestates25.data.auth

import io.ktor.client.HttpClient

import io.ktor.client.request.post
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.model.User
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ResponseException
import io.ktor.http.contentType
import io.ktor.client.request.accept
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import org.json.JSONException
import org.json.JSONObject


class AuthApiImpl(private val httpClient: HttpClient) : AuthApi {

    override suspend fun signUp(request: AuthRequest) {
        httpClient.post("http://10.0.2.2:8080/signup") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }


    override suspend fun signIn(request: AuthRequest): TokenResponse {
        Log.d("AuthApiImpl", "Invio richiesta SignIn con email: ${request.email}")
        return try {
            val response: TokenResponse = httpClient.post("http://10.0.2.2:8080/signin") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }.body()
            Log.d("AuthApiImpl", "Risposta ricevuta: token = ${response.token}")
            response
        } catch (e: ResponseException) {
            Log.e("AuthApiImpl", "Errore nella risposta del server: ${e.response.status}", e)
            throw e
        } catch (e: NoTransformationFoundException) {
            Log.e("AuthApiImpl", "Risposta non valida per deserializzazione", e)
            throw e
        }
    }


    override suspend fun authenticate(token: String): HttpResponse {
        try {
            return httpClient.get("http://10.0.2.2:8080/authenticate") {
                header("Authorization", "Bearer $token")
            }
        } catch (e: ResponseException) {
            Log.e("AuthApiImpl", "Errore nella risposta: ${e.response.status}", e)
            throw e
        } catch (e: Exception) {
            Log.e("AuthApiImpl", "Errore sconosciuto durante authenticate", e)
            throw e
        }
    }


    override suspend fun fetchStateKtor(): String {
        return try {
            // Effettua una richiesta GET all'endpoint del server
            val response: HttpResponse = httpClient.get("http://10.0.2.2:8080/generate-state")

            // Estrai il corpo della risposta come testo
            val state = response.bodyAsText()
            if (state.isNotEmpty()) {
                Log.d("AuthApi", "Stato recuperato: $state")
                state // Restituisci lo stato
            } else {
                throw IllegalStateException("La risposta è vuota.")
            }
        } catch (e: ResponseException) {
            Log.e("AuthApi", "Errore HTTP: ${e.response.status.value} - ${e.response.status.description}", e)
            throw e // Propaga l'errore HTTP
        } catch (e: JSONException) {
            Log.e("AuthApi", "Errore durante il parsing della risposta JSON", e)
            throw IllegalStateException("Risposta JSON non valida", e)
        } catch (e: Exception) {
            Log.e("AuthApi", "Errore sconosciuto durante il fetch dello stato", e)
            throw IllegalStateException("Errore generico nel fetch dello stato", e)
        }
    }
    override suspend fun notifyServer(code: String?, state: String?): User {
        // Controllo dei parametri
        if (code.isNullOrBlank() || state.isNullOrBlank()) {
            Log.e("OAuth", "Parametri mancanti: code o state sono null o vuoti")
            throw IllegalArgumentException("Code o state non possono essere null o vuoti")
        }

        Log.d("OAuth", "Invio richiesta per scambio codice: code=$code, state=$state")

        return try {
            // Effettua la richiesta POST al server
            val response: User = httpClient.post("http://10.0.2.2:8080/auth/github") {
                contentType(ContentType.Application.Json) // Imposta il Content-Type
                accept(ContentType.Application.Json)     // Specifica l'accettazione del JSON
                setBody(mapOf("code" to code, "state" to state)) // Corpo della richiesta
            }.body() // Deserializza la risposta come User

            Log.d("OAuth", "Utente ricevuto con successo: $response")
            response
        } catch (e: ResponseException) {
            Log.e("OAuth", "Errore HTTP: ${e.response.status}", e)
            throw e // Propaga l'errore HTTP
        } catch (e: Exception) {
            Log.e("OAuth", "Errore generico: ${e.localizedMessage}", e)
            throw e // Propaga errori generici
        }
    }

    /*override suspend fun notifyServer(code: String?, state: String?): TokenResponse {
        if (code.isNullOrBlank() || state.isNullOrBlank()) {
            Log.e("OAuth", "Parametri mancanti: code o state sono null o vuoti")
            throw IllegalArgumentException("Code o state non possono essere null o vuoti")
        }

        Log.d("OAuth", "Invio richiesta per scambio codice: code=$code, state=$state")

        return try {
            val response: TokenResponse = httpClient.post("http://10.0.2.2:8080/auth/github") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(mapOf("code" to code, "state" to state))
            }.body()

            Log.d("OAuth", "Access Token ricevuto con successo")
            response
        } catch (e: ResponseException) {
            Log.e("OAuth", "Errore nella risposta del server: ${e.response.status}", e)
            throw e
        } catch (e: NoTransformationFoundException) {
            Log.e("OAuth", "Risposta non valida per deserializzazione", e)
            throw e
        } catch (e: Exception) {
            Log.e("OAuth", "Errore generico: ${e.localizedMessage}", e)
            throw e
        }
    }*/





}
