package com.example.ingsw_24_25_dietiestates25.data.auth

import io.ktor.client.HttpClient

import io.ktor.client.request.post
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import android.util.Log
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.http.contentType
import io.ktor.client.request.accept


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

    override suspend fun getGitHubAccessToken(code: String): TokenResponse {
        Log.d("AuthApiImpl", "Richiesta per ottenere l'access token da GitHub con il codice: $code")
        return try {
            val response: TokenResponse = httpClient.post("http://10.0.2.2:8080/github/token") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("code" to code)) // Passa il codice come corpo della richiesta
            }.body()

            Log.d("AuthApiImpl", "Access token ricevuto: ${response.token}")
            response
        } catch (e: ResponseException) {
            Log.e("AuthApiImpl", "Errore HTTP durante la richiesta: ${e.response.status}", e)
            throw e
        } catch (e: Exception) {
            Log.e("AuthApiImpl", "Errore generico durante la richiesta del token", e)
            throw e
        }
    }



}
