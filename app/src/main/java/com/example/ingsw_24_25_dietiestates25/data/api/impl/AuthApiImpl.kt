package com.example.ingsw_24_25_dietiestates25.data.api.impl

import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.api.AuthApi
import com.example.ingsw_24_25_dietiestates25.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.response.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.json.JSONException
import javax.inject.Inject

class AuthApiImpl @Inject constructor (private val httpClient: HttpClient) : AuthApi {

    override suspend fun signUp(request: AuthRequest): TokenResponse {
        Log.d("AuthApiImpl", "Tentativo di SignUp")
        return try {
            val response: TokenResponse = httpClient.post("http://10.0.2.2:8080/auth/signup") {
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

    override suspend fun authWithThirdParty(request: AuthRequest): TokenResponse {
        Log.d("AuthApiImpl", "Invio richiesta OAUTH")
        return try {
            val response: TokenResponse = httpClient.post("http://10.0.2.2:8080/auth/thirdPartyUser") {
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

    override suspend fun signIn(request: AuthRequest): TokenResponse {
        Log.d("AuthApiImpl", "Invio richiesta SignIn con email: ${request.email}")
        return try {
            val response: TokenResponse = httpClient.post("http://10.0.2.2:8080/auth/signin") {
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

    override suspend fun resetPassword(request: AuthRequest) {
        httpClient.post("http://10.0.2.2:8080/auth/reset-password") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    override suspend fun fetchGitHubState(): String {
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
            Log.e(
                "AuthApi",
                "Errore HTTP: ${e.response.status.value} - ${e.response.status.description}",
                e
            )
            throw e // Propaga l'errore HTTP
        } catch (e: JSONException) {
            Log.e("AuthApi", "Errore durante il parsing della risposta JSON", e)
            throw IllegalStateException("Risposta JSON non valida", e)
        } catch (e: Exception) {
            Log.e("AuthApi", "Errore sconosciuto durante il fetch dello stato", e)
            throw IllegalStateException("Errore generico nel fetch dello stato", e)
        }
    }

    override suspend fun exchangeGitHubCode(code: String?, state: String?): TokenResponse {
        // Controllo dei parametri
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
            Log.d("AuthApiImpl", "Risposta ricevuta: token = ${response.token}")
            response

        } catch (e: ResponseException) {
            Log.e("OAuth", "Errore HTTP: ${e.response.status}", e)
            throw e // Propaga l'errore HTTP
        } catch (e: Exception) {
            Log.e("OAuth", "Errore generico: ${e.localizedMessage}", e)
            throw e // Propaga errori generici
        }
    }






}