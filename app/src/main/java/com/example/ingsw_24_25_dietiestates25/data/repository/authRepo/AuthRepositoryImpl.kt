package com.example.ingsw_24_25_dietiestates25.data.repository.authRepo

import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.api.authApi.AuthApi
import com.example.ingsw_24_25_dietiestates25.data.api.imageApi.ImageApi
import com.example.ingsw_24_25_dietiestates25.data.jwt.parseJwtPayload
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.result.AuthResult
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.model.request.ImageRequest
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor (
    private val api: AuthApi,
    private val sessionManager: UserSessionManager
): AuthRepository {

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9]+@[A-Za-z0-9]+\\.(?:it|com)$")
        return emailRegex.matches(email)
    }

    override suspend fun resetPassword(email: String, oldPassword: String, newPassword: String): AuthResult<Unit> {
        return try {
            api.resetPassword(AuthRequest(email = email, password = oldPassword,  newPassword = newPassword))
            AuthResult.Success(Unit, "Operation Successfull, Password changed")
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> AuthResult.Unauthorized("Reset failed ")
                else -> AuthResult.UnknownError("Errore")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Errore generico")
        }
    }

    override suspend fun sendAgencyRequest(email: String, password: String, agencyName: String): AuthResult<Unit> {
        return try {
            api.sendAgencyRequest(AuthRequest(email = email, password = password, agencyName = agencyName))
            AuthResult.Success(Unit, "Operation Successfull , You can now log in. You can check the status of your request in your user profile.")
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> AuthResult.Unauthorized("Reset failed ")
                else -> AuthResult.UnknownError("Errore")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Errore generico")
        }
    }

    override suspend fun signUp(email: String, password: String, profilePicBase64: String): AuthResult<Unit> {

        if (!isValidEmail(email)) {
            return AuthResult.Unauthorized("Email non valida")
        }

        return try {
            // Invia la richiesta di registrazione e ricevi il token
            val response = api.signUp(AuthRequest(email = email, password = password))
            val token = response.token

            sessionManager.saveToken(token)

            AuthResult.Authorized()

        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.BadRequest -> AuthResult.UnknownError("Dati mancanti o formattazione errata.")
                HttpStatusCode.Conflict -> AuthResult.Unauthorized("Email già registrata")
                else -> AuthResult.UnknownError("Errore HTTP: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun authWithThirdParty(email: String, username: String): AuthResult<Unit> {
        return try {

            val tokenResponse = api.authWithThirdParty(AuthRequest(email = email, username = username))

            sessionManager.saveToken(tokenResponse.token)

            AuthResult.Authorized()

        } catch (e: ResponseException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError("Errore HTTP")
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Errore generico")
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult<Unit> {
        return try {
            // 1. Chiama l’API e ottiene un TokenResponse
            val tokenResponse = api.signIn(AuthRequest(email = email, password = password))

            // 2. Salva il token ricevuto
            sessionManager.saveToken(tokenResponse.token)

            // 3. Restituisce il risultato positivo
            AuthResult.Authorized()

        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized,
                HttpStatusCode.Conflict -> {
                    AuthResult.Unauthorized("Accesso fallito: credenziali errate.")
                }
                else -> {
                    AuthResult.UnknownError("Errore HTTP: ${e.response.status.value}")
                }
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun getLoggedUser(): AuthResult<Unit> {
        val token = sessionManager.getToken()
            ?: return AuthResult.Unauthorized("Token mancante.")

        return try {
            val responseUser = api.getLoggedUser("Bearer $token")

            val user = User(
                id = responseUser.id,
                name = responseUser.name,
                surname = responseUser.surname,
                email = responseUser.email,
                username = responseUser.username,
                type = responseUser.type,
            )

            sessionManager.saveUser(user, token)

            AuthResult.Authorized()
        } catch (e: Exception) {
            AuthResult.UnknownError("Errore nel recupero profilo: ${e.localizedMessage}")
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

    override suspend fun exchangeGitHubCode(code: String?, state: String?): AuthResult<Unit> {
        return try {
            Log.d("AuthRepository", "Inizio notify con code=$code e state=$state")

            val response = api.exchangeGitHubCode(code, state) // Chiamata diretta all'API per ottenere l'utente
            val token = response.token

            // 2) decodifica il payload JWT
            val payload = parseJwtPayload(token)

            // 3) salva username e token nella sessione
            sessionManager.saveUser(
                User(
                    username = payload.username!!,
                    id = payload.userId,
                    email = payload.email!!,
                    type = payload.type!!
                ), token)

            AuthResult.Authorized()
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
