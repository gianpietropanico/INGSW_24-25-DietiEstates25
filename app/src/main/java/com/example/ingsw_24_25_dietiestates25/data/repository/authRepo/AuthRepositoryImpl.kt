package com.example.ingsw_24_25_dietiestates25.data.repository.authRepo

import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.parseJwtPayload
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AgencyUser
import com.example.ingsw_24_25_dietiestates25.data.model.request.AuthRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import javax.inject.Inject
import com.example.ingsw_24_25_dietiestates25.data.model.response.TokenResponse
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.json.JSONException



class AuthRepositoryImpl @Inject constructor (
    private val httpClient: HttpClient,
    private val sessionManager: UserSessionManager
): AuthRepository {

    private val baseURL = "http://10.0.2.2:8080"
    private val userSessionManager = sessionManager
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9]+@[A-Za-z0-9]+\\.(?:it|com)$")
        return emailRegex.matches(email)
    }


    override suspend fun sendAgencyRequest(email: String, password: String, agencyName: String): ApiResult<AgencyUser> {
        if (!isValidEmail(email)) {
            return ApiResult.Unauthorized("Email non valida")
        }

        return try {
            val request = AuthRequest(email = email, password = password, agencyName = agencyName)

            Log.d("AuthApiImpl", "Invio richiesta AgencySignIn con email: ${request.email}")
            val response = httpClient.post("$baseURL/agency/request") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val agencyUser = response.body<AgencyUser>()
                    Log.d("AuthApiImpl", "Richiesta inviata con successo: $agencyUser")
                    ApiResult.Success(agencyUser, "Operazione completata con successo")
                }

                HttpStatusCode.Conflict, HttpStatusCode.BadRequest, HttpStatusCode.Unauthorized -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Richiesta agency fallita: $err")
                    ApiResult.Unauthorized("Richiesta agency fallita: $err")
                }

                else -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Errore HTTP ${response.status}: $err")
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Richiesta agency fallita")
                else -> ApiResult.UnknownError("Errore HTTP: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun signUp(email: String, password: String): ApiResult<Unit> {
        if (!isValidEmail(email)) {
            return ApiResult.Unauthorized("Email non valida")
        }

        return try {
            val request = AuthRequest(email = email, password = password)

            Log.d("AuthApiImpl", "Invio richiesta SignUp con email: ${request.email}")
            val response = httpClient.post("$baseURL/auth/signup") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val tokenResp = response.body<TokenResponse>()
                    Log.d("AuthApiImpl", "Risposta ricevuta: token = ${tokenResp.token}")

                    userSessionManager.saveToken(tokenResp.token)
                    ApiResult.Authorized()
                }
                HttpStatusCode.Conflict, HttpStatusCode.Unauthorized -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Credenziali errate: $err")
                    ApiResult.Unauthorized("Email già registrata")
                }
                HttpStatusCode.BadRequest -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Richiesta errata: $err")
                    ApiResult.UnknownError("Dati mancanti o formattazione errata.")
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Errore HTTP ${response.status}: $err")
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}")
                }
            }
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
            val request = AuthRequest(email = email, username = username)

            Log.d("AuthApiImpl", "Invio richiesta OAUTH con email: ${request.email}")
            val response = httpClient.post("$baseURL/auth/thirdPartyUser") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val tokenResp = response.body<TokenResponse>()
                    Log.d("AuthApiImpl", "Risposta ricevuta: token = ${tokenResp.token}")

                    userSessionManager.saveToken(tokenResp.token)
                    ApiResult.Authorized()
                }
                HttpStatusCode.Conflict, HttpStatusCode.Unauthorized -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Credenziali errate: $err")
                    ApiResult.Unauthorized("Accesso negato")
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Errore HTTP ${response.status}: $err")
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                ApiResult.Unauthorized()
            } else {
                ApiResult.UnknownError("Errore HTTP")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun signIn(email: String, password: String): ApiResult<Unit> {
        return try {
            val request = AuthRequest(email = email, password = password)

            Log.d("AuthApiImpl", "Invio richiesta SignIn con email: ${request.email}")
            val response = httpClient.post("$baseURL/auth/signin") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val tokenResponse = response.body<TokenResponse>()
                    Log.d("AuthApiImpl", "Token ricevuto: ${tokenResponse.token}")

                    userSessionManager.saveToken(tokenResponse.token)
                    ApiResult.Authorized()
                }

                HttpStatusCode.Conflict, HttpStatusCode.Unauthorized -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Accesso fallito: $err")
                    ApiResult.Unauthorized("Accesso fallito: credenziali errate.")
                }

                else -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Errore HTTP ${response.status}: $err")
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized,
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Accesso fallito: credenziali errate.")
                else -> ApiResult.UnknownError("Errore HTTP: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun getLoggedUser(): ApiResult<Unit> {
        val token = userSessionManager.getToken()
            ?: return ApiResult.Unauthorized("Token mancante.")

        return try {
            val response = httpClient.get("$baseURL/auth/me") {
                header("Authorization", "Bearer $token")
                accept(ContentType.Application.Json)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val responseUser: User = response.body()

                    val user = User(
                        id = responseUser.id,
                        name = responseUser.name,
                        surname = responseUser.surname,
                        email = responseUser.email,
                        username = responseUser.username,
                        role = responseUser.role,
                    )

                    userSessionManager.saveUser(user, token)
                    ApiResult.Authorized()
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Errore HTTP ${response.status}: $err")
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Errore nel recupero profilo: ${e.localizedMessage}")
        }
    }

    override suspend fun setUpAgency(userId: String): ApiResult<Unit> {
        return try {
            val response = httpClient.get("$baseURL/agency") {
                accept(ContentType.Application.Json)
                parameter("userId", userId)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val agency: Agency = response.body()
                    userSessionManager.saveAgency(agency)
                    ApiResult.Success(Unit, "Agenzia caricata e salvata correttamente")
                }
                HttpStatusCode.NotFound -> {
                    val msg = "Nessuna agenzia trovata per userId=$userId"
                    println(msg)
                    ApiResult.UnknownError(msg)
                }
                else -> {
                    val err = response.bodyAsText()
                    println("Errore HTTP ${response.status.value}: $err")
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
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

            val response: HttpResponse = httpClient.get("$baseURL/generate-state")
            val state = response.bodyAsText()

            return if (response.status == HttpStatusCode.OK && state.isNotEmpty()) {
                Log.d("AuthRepository", "Stato generato con successo: $state")
                ApiResult.Success(state)
            } else {
                val err = response.bodyAsText()
                Log.e("AuthRepository", "Errore HTTP ${response.status}: $err")
                ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
            }
        } catch (e: ResponseException) {
            Log.e("AuthRepository", "Errore HTTP: ${e.response.status}", e)
            when (e.response.status) {
                HttpStatusCode.BadRequest -> ApiResult.Unauthorized("Richiesta non valida")
                HttpStatusCode.Unauthorized -> ApiResult.Unauthorized("Accesso negato")
                else -> ApiResult.UnknownError("Errore HTTP ${e.response.status.value}")
            }
        } catch (e: JSONException) {
            Log.e("AuthRepository", "Errore durante il parsing della risposta JSON", e)
            ApiResult.UnknownError("Risposta JSON non valida")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Errore generale durante FetchState", e)
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

    override suspend fun githubOauth(code: String?, state: String?): ApiResult<Unit> {
        // Controllo preliminare dei parametri
        if (code.isNullOrBlank() || state.isNullOrBlank()) {
            Log.e("OAuth", "Parametri mancanti: code o state sono null o vuoti")
            return ApiResult.UnknownError("Code o state non possono essere null o vuoti")
        }

        return try {
            Log.d("OAuth", "Invio richiesta per scambio codice: code=$code, state=$state")

            val response: HttpResponse = httpClient.post("$baseURL/auth/github") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(mapOf("code" to code, "state" to state))
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val tokenResponse: TokenResponse = response.body()
                    Log.d("AuthApiImpl", "Risposta ricevuta: token = ${tokenResponse.token}")

                    val payload = parseJwtPayload(tokenResponse.token)

                    userSessionManager.saveUser(
                        User(
                            username = payload.username!!,
                            id = payload.userId,
                            name = payload.name,
                            surname = payload.surname,
                            email = payload.email!!,
                            role = payload.role!!
                        ),
                        tokenResponse.token
                    )

                    ApiResult.Authorized()
                }
                HttpStatusCode.BadRequest,
                HttpStatusCode.Unauthorized -> {
                    val err = response.bodyAsText()
                    Log.e("OAuth", "Autenticazione fallita: $err")
                    ApiResult.Unauthorized("Errore di autorizzazione: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("OAuth", "Errore HTTP ${response.status}: $err")
                    ApiResult.UnknownError("Errore HTTP ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            Log.e("OAuth", "Errore HTTP: ${e.response.status}", e)
            when (e.response.status) {
                HttpStatusCode.BadRequest -> ApiResult.Unauthorized("Richiesta non valida")
                HttpStatusCode.Unauthorized -> ApiResult.Unauthorized("Accesso negato")
                else -> ApiResult.UnknownError("Errore HTTP ${e.response.status.value}")
            }
        } catch (e: Exception) {
            Log.e("OAuth", "Errore generico durante GitHub OAuth: ${e.localizedMessage}", e)
            ApiResult.UnknownError("Errore generico: ${e.localizedMessage}")
        }
    }

}
