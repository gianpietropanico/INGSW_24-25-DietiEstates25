package com.example.ingsw_24_25_dietiestates25.data.api.impl

import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.api.AuthApi
import com.example.ingsw_24_25_dietiestates25.model.authenticate.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.authenticate.AuthResponse
import com.example.ingsw_24_25_dietiestates25.model.authenticate.TokenResponse
import com.example.ingsw_24_25_dietiestates25.model.authenticate.User
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.json.JSONException
import javax.inject.Inject

class AuthApiImpl @Inject constructor (private val httpClient: HttpClient) : AuthApi {

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

    override suspend fun exchangeCodeForJwt(code: String): String? {
        return try {
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8080/authenticate") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("code" to code))
            }
            if (response.status == HttpStatusCode.OK) {
                response.body<Map<String, String>>()["jwt"] // Estrai il JWT dalla risposta
            } else {
                Log.e("Auth", "Errore durante lo scambio del code: ${response.status}")
                null
            }
        } catch (e: Exception) {
            Log.e("Auth", "Errore durante lo scambio del code: ${e.localizedMessage}")
            null
        }
    }

    override suspend fun fetchAuthResponse(code: String): AuthResponse? {
        return try {
            // Effettua la richiesta al server per scambiare il codice con il token JWT e il payload utente
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8080/authenticate") {
                contentType(ContentType.Application.Json) // Imposta il tipo di contenuto a JSON
                setBody(mapOf("code" to code)) // Invia il codice come payload della richiesta
            }

            // Controlla lo stato della risposta
            if (response.status == HttpStatusCode.OK) {
                // Deserializza il corpo della risposta in un oggetto AuthResponse
                response.body()
            } else {
                Log.e("Auth", "Errore durante l'autenticazione: ${response.status}")
                null
            }
        } catch (e: Exception) {
            // Log degli errori in caso di problemi nella richiesta
            Log.e("Auth", "Errore: ${e.localizedMessage}")
            null
        }
    }


    override suspend fun fetchJwtFromServer(code: String): String? {
        return try {
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8080/callback/github") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("code" to code))
            }
            if (response.status == HttpStatusCode.OK) {
                response.body<Map<String, String>>()["jwt"]
            } else {
                Log.e("AuthRepository", "Errore durante la richiesta del JWT: ${response.status}")
                null
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Errore durante la richiesta del JWT: ${e.localizedMessage}")
            null
        }
    }
}