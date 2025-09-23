package com.example.ingsw_24_25_dietiestates25.data.repository.authRepo

import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.api.authApi.AuthApi
import com.example.ingsw_24_25_dietiestates25.data.jwt.parseJwtPayload
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.model.dataclass.AgencyUser
import com.example.ingsw_24_25_dietiestates25.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor (
    private val authApi: AuthApi,
    private val sessionManager: UserSessionManager
): AuthRepository {

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9]+@[A-Za-z0-9]+\\.(?:it|com)$")
        return emailRegex.matches(email)
    }


    override suspend fun sendAgencyRequest( email: String, password: String, agencyName: String): ApiResult<AgencyUser> {

        if (!isValidEmail(email)) {
            return ApiResult.Unauthorized("Email non valida")
        }

        return try {
            val agencyUser = authApi.sendAgencyRequest(
                AuthRequest(email = email, password = password, agencyName = agencyName)
            )

            ApiResult.Success(agencyUser, "Operazione completata con successo")

        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Richiesta agency fallita")
                else -> ApiResult.UnknownError("Errore")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico")
        }
    }

    override suspend fun signUp(email: String, password: String): ApiResult<Unit> {

        if (!isValidEmail(email)) {
            return ApiResult.Unauthorized("Email non valida")
        }

        return try {

            val response = authApi.signUp(AuthRequest(email = email, password = password))
            val token = response.token

            sessionManager.saveToken(token)

            ApiResult.Authorized()

        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.BadRequest -> ApiResult.UnknownError("Dati mancanti o formattazione errata.")
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Email già registrata")
                else -> ApiResult.UnknownError("Errore HTTP: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun authWithThirdParty(email: String, username: String): ApiResult<Unit> {
        return try {

            val tokenResponse = authApi.authWithThirdParty(AuthRequest(email = email, username = username))

            sessionManager.saveToken(tokenResponse.token)

            ApiResult.Authorized()

        } catch (e: ResponseException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                ApiResult.Unauthorized()
            } else {
                ApiResult.UnknownError("Errore HTTP")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico")
        }
    }

    override suspend fun signIn(email: String, password: String): ApiResult<Unit> {
        return try {
            // 1. Chiama l’API e ottiene un TokenResponse
            val tokenResponse = authApi.signIn(AuthRequest(email = email, password = password))

            // 2. Salva il token ricevuto
            sessionManager.saveToken(tokenResponse.token)

            // 3. Restituisce il risultato positivo
            ApiResult.Authorized()

        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized,
                HttpStatusCode.Conflict -> {
                    ApiResult.Unauthorized("Accesso fallito: credenziali errate.")
                }
                else -> {
                    ApiResult.UnknownError("Errore HTTP: ${e.response.status.value}")
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun getLoggedUser(): ApiResult<Unit> {
        val token = sessionManager.getToken()
            ?: return ApiResult.Unauthorized("Token mancante.")

        return try {
            val responseUser = authApi.getLoggedUser("Bearer $token")

            val user = User(
                id = responseUser.id,
                name = responseUser.name,
                surname = responseUser.surname,
                email = responseUser.email,
                username = responseUser.username,
                role = responseUser.role,
            )

            sessionManager.saveUser(user, token)

            ApiResult.Authorized()
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore nel recupero profilo: ${e.localizedMessage}")
        }
    }

    override suspend fun setUpAgency(userId: String): ApiResult<Unit> {
        return try {
            val response = authApi.getAgency(userId)

            if (response != null) {
                sessionManager.saveAgency(response)

                ApiResult.Success(Unit, "Agenzia caricata e salvata correttamente")

            } else {
                ApiResult.UnknownError("Nessuna agenzia trovata per userId=$userId")
            }

        } catch (e: ClientRequestException) {
            ApiResult.UnknownError("Errore HTTP ${e.response.status.value}: ${e.response.bodyAsText()}")
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore nel recupero agenzia: ${e.localizedMessage}")
        }
    }

    override suspend fun fetchState(): ApiResult<String> {
        return try {
            Log.d("AuthRepository", "Inizio FetchState")
            val state = authApi.fetchGitHubState() // Chiamata diretta all'API per ottenere lo stato

            Log.d("AuthRepository", "Stato generato con successo: $state")

            ApiResult.Success(state) // Incapsula lo stato nel risultato
        } catch (e: ResponseException) {
            Log.e("AuthRepository", "Errore HTTP: ${e.response.status}", e)
            when (e.response.status) {
                HttpStatusCode.BadRequest -> ApiResult.Unauthorized() // Errore di richiesta
                HttpStatusCode.Unauthorized -> ApiResult.Unauthorized() // Errore di autorizzazione
                else -> ApiResult.UnknownError() // Errore generico
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Errore generale durante FetchState", e)
            ApiResult.UnknownError() // Errore generico in caso di eccezione
        }
    }

    override suspend fun githubOauth(code: String?, state: String?): ApiResult<Unit> {
        return try {
            Log.d("AuthRepository", "Inizio notify con code=$code e state=$state")

            val response = authApi.githubOauth(code, state) // Chiamata diretta all'API per ottenere l'utente
            val token = response.token

            val payload = parseJwtPayload(token)

            sessionManager.saveUser(
                User(
                    username = payload.username!!,
                    id = payload.userId,
                    name = payload.name,
                    surname = payload.surname,
                    email = payload.email!!,
                    role = payload.role!!
                ), token)

            ApiResult.Authorized()

        } catch (e: ResponseException) {
            Log.e("AuthRepository", "Errore HTTP: ${e.response.status}", e)
            when (e.response.status) {
                HttpStatusCode.BadRequest -> ApiResult.Unauthorized() // Errore di richiesta
                HttpStatusCode.Unauthorized -> ApiResult.Unauthorized() // Errore di autorizzazione
                else -> ApiResult.UnknownError() // Errore generico
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Errore generale durante notifyServer", e)
            ApiResult.UnknownError() // Errore generico in caso di eccezione
        }
    }

}
