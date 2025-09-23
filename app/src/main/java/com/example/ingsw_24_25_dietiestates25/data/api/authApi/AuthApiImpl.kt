package com.example.ingsw_24_25_dietiestates25.data.api.authApi

import android.util.Log
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.model.dataclass.AgencyUser
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.response.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
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

    override suspend fun signUp(request: AuthRequest): TokenResponse {
        Log.d("AuthApiImpl", "Invio richiesta SignUp con email: ${request.email}")
        val response = httpClient.post("http://10.0.2.2:8080/auth/signup") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(request)
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val tokenResp = response.body<TokenResponse>()
                Log.d("AuthApiImpl", "Risposta ricevuta: token = ${tokenResp.token}")
                tokenResp
            }
            HttpStatusCode.Conflict, HttpStatusCode.Unauthorized -> {
                val err = response.bodyAsText()
                Log.e("AuthApiImpl", "Credenziali errate: $err")
                throw ResponseException(response, "Registrazione fallita: $err")
            }
            else -> {
                val err = response.bodyAsText()
                Log.e("AuthApiImpl", "Errore HTTP ${response.status}: $err")
                throw ResponseException(response, "Errore HTTP ${response.status}")
            }
        }
    }

    override suspend fun authWithThirdParty(request: AuthRequest): TokenResponse {
        Log.d("AuthApiImpl", "Invio richiesta OAUTH con email: ${request.email}")
        val response = httpClient.post("http://10.0.2.2:8080/auth/thirdPartyUser") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(request) 
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val tokenResp = response.body<TokenResponse>()
                Log.d("AuthApiImpl", "Risposta ricevuta: token = ${tokenResp.token}")
                tokenResp
            }
            HttpStatusCode.Conflict, HttpStatusCode.Unauthorized -> {
                val err = response.bodyAsText()
                Log.e("AuthApiImpl", "Credenziali errate: $err")
                throw ResponseException(response, "Registrazione fallita: $err")
            }
            else -> {
                val err = response.bodyAsText()
                Log.e("AuthApiImpl", "Errore HTTP ${response.status}: $err")
                throw ResponseException(response, "Errore HTTP ${response.status}")
            }
        }
    }

    override suspend fun signIn(request: AuthRequest): TokenResponse {
        Log.d("AuthApiImpl", "Invio richiesta SignIn con email: ${request.email}")

        val response = httpClient.post("http://10.0.2.2:8080/auth/signin") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(request)
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val tokenResponse = response.body<TokenResponse>()
                Log.d("AuthApiImpl", "Token ricevuto: ${tokenResponse.token}")
                tokenResponse
            }

            HttpStatusCode.Conflict, HttpStatusCode.Unauthorized -> {
                val err = response.bodyAsText()
                Log.e("AuthApiImpl", "Accesso fallito: $err")
                throw ResponseException(response, "Accesso fallito: $err")
            }

            else -> {
                val err = response.bodyAsText()
                Log.e("AuthApiImpl", "Errore HTTP ${response.status}: $err")
                throw ResponseException(response, "Errore HTTP ${response.status}")
            }
        }
    }

    override suspend fun sendAgencyRequest(request: AuthRequest): AgencyUser {
        Log.d("AuthApiImpl", "Invio richiesta AgencySignIn con email: ${request.email}")

        val response = httpClient.post("http://10.0.2.2:8080/agency/request") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(request)
        }

        return when (response.status) {
            HttpStatusCode.OK, HttpStatusCode.Created -> {
                val agencyUser = response.body<AgencyUser>()
                Log.d("AuthApiImpl", "Richiesta inviata con successo: $agencyUser")
                agencyUser
            }

            HttpStatusCode.Conflict, HttpStatusCode.BadRequest, HttpStatusCode.Unauthorized -> {
                val err = response.bodyAsText()
                Log.e("AuthApiImpl", "Richiesta agency fallita: $err")
                throw ResponseException(response, "Richiesta agency fallita: $err")
            }

            else -> {
                val err = response.bodyAsText()
                Log.e("AuthApiImpl", "Errore HTTP ${response.status}: $err")
                throw ResponseException(response, "Errore HTTP ${response.status}")
            }
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

    override suspend fun githubOauth(code: String?, state: String?): TokenResponse {
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

    override suspend fun getLoggedUser(authHeader: String): User {
        val response = httpClient.get("http://10.0.2.2:8080/auth/me") {
            header("Authorization", authHeader)
            accept(ContentType.Application.Json)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            else -> {
                val err = response.bodyAsText()
                Log.e("AuthApiImpl", "Errore HTTP ${response.status}: $err")
                throw ResponseException(response, "Errore HTTP ${response.status}")
            }
        }
    }

    override suspend fun getAgency(userId: String): Agency? {
        val response = httpClient.get("http://10.0.2.2:8080/agency") {
            accept(ContentType.Application.Json)
            parameter("userId", userId)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body<Agency>()
            HttpStatusCode.NotFound -> {
                println("Nessuna agenzia trovata per userId=$userId")
                null
            }
            else -> {
                val err = response.bodyAsText()
                println("Errore HTTP ${response.status.value}: $err")
                null
            }
        }
    }


}