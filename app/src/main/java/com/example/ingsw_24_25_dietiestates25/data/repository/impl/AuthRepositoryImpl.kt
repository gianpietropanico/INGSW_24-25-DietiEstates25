package com.example.ingsw_24_25_dietiestates25.data.repository.impl

import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.api.AuthApi
import com.example.ingsw_24_25_dietiestates25.data.jwt.parseJwtPayload
import com.example.ingsw_24_25_dietiestates25.data.repository.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.authenticate.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.authenticate.AuthResult
import com.example.ingsw_24_25_dietiestates25.model.authenticate.User
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import org.json.JSONObject
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor (
    private val api: AuthApi,
    private val sessionManager: UserSessionManager
): AuthRepository {

    override suspend fun resetPassword(email: String, oldPassword: String, newPassword: String): AuthResult<Unit> {
        return try {
            api.resetPassword(AuthRequest(email = email, password = oldPassword,  newPassword = newPassword))
            AuthResult.Success()
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> AuthResult.Unauthorized("Reset failed: ${e.message}")
                else -> AuthResult.UnknownError("Errore: ${e.response.status}")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun signUp(email: String, password: String): AuthResult<Unit> {
        return try {
            api.signUp(
                request = AuthRequest(email = email, password = password)
            )

            AuthResult.Success()

        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.BadRequest -> AuthResult.UnknownError("Dati mancanti o formattazione errata.")
                HttpStatusCode.Conflict -> AuthResult.Unauthorized("Email già registrata o password troppo corta.")
                else -> AuthResult.UnknownError("Errore: ${e.response.status}")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Eccezione generica: ${e.localizedMessage}")
        }
    }

    override suspend fun authWithThirdParty(email: String, username: String): AuthResult<Unit> {
        return try {
            // 1) fai la chiamata e ottieni il token
            val response = api.authWithThirdParty(AuthRequest(email = email, username = username))
            val token = response.token

            // 2) decodifica il payload JWT
            val payload = parseJwtPayload(token)

            // 3) salva username e token nella sessione
            sessionManager.saveUsernameSession(payload.username, token)

            AuthResult.Authorized()

        } catch (e: ResponseException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError("Errore HTTP: ${e.response.status}")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Eccezione: ${e.message}")
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult<Unit> {
        return try {
            // 1) fai la chiamata e ottieni il token
            val response = api.signIn(AuthRequest( email = email, password = password))
            val token = response.token

            // 2) decodifica il payload
            val payload = parseJwtPayload(token)

            // 3) salva solo il userId e il token (il resto non è nel token)
            sessionManager.saveUsernameSession(payload.username, token)

            AuthResult.Authorized()
        } catch (e: ResponseException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError("Errore HTTP: ${e.response.status}")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Eccezione: ${e.message}")
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        val token = sessionManager.token.value ?: return AuthResult.Unauthorized()

        return try {
            api.authenticate("Bearer $token")
            AuthResult.Success()

        } catch (e: ResponseException) { // Gestisce le eccezioni HTTP
            if (e.response.status == HttpStatusCode.Unauthorized) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) { // Gestisce altre eccezioni
            AuthResult.UnknownError()
        }
    }


    override suspend fun logout() {
        try {
            sessionManager.clear()
            Log.d("AuthRepository", "Logout completato con successo")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Errore durante il logout", e)
        }
    }


    override suspend fun fetchState(): AuthResult<String> {
        return try {
            Log.d("AuthRepository", "Inizio FetchState")
            val state = api.fetchGitHubState() // Chiamata diretta all'API per ottenere lo stato

            Log.d("AuthRepository", "Stato generato con successo: $state")

            AuthResult.Success(state) // Incapsula lo stato nel risultato
        } catch (e: ResponseException) {
            Log.e("AuthRepository", "Errore HTTP: ${e.response.status}", e)
            when (e.response.status) {
                HttpStatusCode.BadRequest -> AuthResult.Unauthorized() // Errore di richiesta
                HttpStatusCode.Unauthorized -> AuthResult.Unauthorized() // Errore di autorizzazione
                else -> AuthResult.UnknownError() // Errore generico
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Errore generale durante FetchState", e)
            AuthResult.UnknownError() // Errore generico in caso di eccezione
        }
    }

    override suspend fun exchangeGitHubCode(code: String?, state: String?): AuthResult<User> {
        return try {
            Log.d("AuthRepository", "Inizio notify con code=$code e state=$state")

            val user = api.exchangeGitHubCode(code, state) // Chiamata diretta all'API per ottenere l'utente

            Log.d("AuthRepository", "Utente ricevuto con successo: ${user}")

            AuthResult.Authorized(user) // Incapsula l'utente nella risposta
        } catch (e: ResponseException) {
            Log.e("AuthRepository", "Errore HTTP: ${e.response.status}", e)
            when (e.response.status) {
                HttpStatusCode.BadRequest -> AuthResult.Unauthorized() // Errore di richiesta
                HttpStatusCode.Unauthorized -> AuthResult.Unauthorized() // Errore di autorizzazione
                else -> AuthResult.UnknownError() // Errore generico
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Errore generale durante notifyServer", e)
            AuthResult.UnknownError() // Errore generico in caso di eccezione
        }
    }



}
