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

    private val baseURL = "http://84.8.252.211:8080/"
    private val userSessionManager = sessionManager

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9]+@[A-Za-z0-9]+\\.(?:it|com)$")
        return emailRegex.matches(email)
    }

    override suspend fun sendAgencyRequest(email: String, password: String, agencyName: String): ApiResult<AgencyUser> {
        if (!isValidEmail(email)) {
            return ApiResult.Unauthorized("Invalid email")
        }

        return try {
            val request = AuthRequest(email = email, password = password, agencyName = agencyName)

            Log.d("AuthApiImpl", "Sending AgencySignIn request with email: ${request.email}")
            val response = httpClient.post("$baseURL/agency/request") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val agencyUser = response.body<AgencyUser>()
                    Log.d("AuthApiImpl", "Request sent successfully: $agencyUser")
                    ApiResult.Success(agencyUser, "Operation completed successfully")
                }

                HttpStatusCode.Conflict, HttpStatusCode.BadRequest, HttpStatusCode.Unauthorized -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Agency request failed: $err")
                    ApiResult.Unauthorized("Agency request failed: $err")
                }

                else -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "HTTP error ${response.status}: $err")
                    ApiResult.UnknownError("HTTP error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Agency request failed")
                else -> ApiResult.UnknownError("HTTP error: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

    override suspend fun signUp(email: String, password: String): ApiResult<Unit> {
        if (!isValidEmail(email)) {
            return ApiResult.Unauthorized("Invalid email")
        }

        return try {
            val request = AuthRequest(email = email, password = password)

            Log.d("AuthApiImpl", "Sending SignUp request with email: ${request.email}")
            val response = httpClient.post("$baseURL/auth/signup") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val tokenResp = response.body<TokenResponse>()
                    Log.d("AuthApiImpl", "Response received: token = ${tokenResp.token}")

                    userSessionManager.saveToken(tokenResp.token)
                    ApiResult.Authorized()
                }
                HttpStatusCode.Conflict, HttpStatusCode.Unauthorized -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Wrong credentials: $err")
                    ApiResult.Unauthorized("Email already registered")
                }
                HttpStatusCode.BadRequest -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Bad request: $err")
                    ApiResult.UnknownError("Missing data or wrong formatting.")
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "HTTP error ${response.status}: $err")
                    ApiResult.UnknownError("HTTP error ${response.status.value}")
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.BadRequest -> ApiResult.UnknownError("Missing data or wrong formatting.")
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Email already registered")
                else -> ApiResult.UnknownError("HTTP error: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

    override suspend fun authWithThirdParty(email: String, username: String): ApiResult<Unit> {
        return try {
            val request = AuthRequest(email = email, username = username)

            Log.d("AuthApiImpl", "Sending OAUTH request with email: ${request.email}")
            val response = httpClient.post("$baseURL/auth/thirdPartyUser") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val tokenResp = response.body<TokenResponse>()
                    Log.d("AuthApiImpl", "Response received: token = ${tokenResp.token}")

                    userSessionManager.saveToken(tokenResp.token)
                    ApiResult.Authorized()
                }
                HttpStatusCode.Conflict, HttpStatusCode.Unauthorized -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Invalid credentials: $err")
                    ApiResult.Unauthorized("Access denied")
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "HTTP error ${response.status}: $err")
                    ApiResult.UnknownError("HTTP error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                ApiResult.Unauthorized()
            } else {
                ApiResult.UnknownError("HTTP error")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

    override suspend fun signIn(email: String, password: String): ApiResult<Unit> {
        return try {
            val request = AuthRequest(email = email, password = password)

            Log.d("AuthApiImpl", "Sending SignIn request with email: ${request.email}")
            val response = httpClient.post("$baseURL/auth/signin") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val tokenResponse = response.body<TokenResponse>()
                    Log.d("AuthApiImpl", "Received token: ${tokenResponse.token}")

                    userSessionManager.saveToken(tokenResponse.token)
                    ApiResult.Authorized()
                }

                HttpStatusCode.Conflict, HttpStatusCode.Unauthorized -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "Login failed: $err")
                    ApiResult.Unauthorized("Login failed: wrong credentials.")
                }

                else -> {
                    val err = response.bodyAsText()
                    Log.e("AuthApiImpl", "HTTP error ${response.status}: $err")
                    ApiResult.UnknownError("HTTP error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized,
                HttpStatusCode.Conflict -> ApiResult.Unauthorized("Login failed: wrong credentials.")
                else -> ApiResult.UnknownError("HTTP error: ${e.response.status.value}")
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

    override suspend fun getLoggedUser(): ApiResult<Unit> {
        val token = userSessionManager.getToken()
            ?: return ApiResult.Unauthorized("Missing token.")

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
                    Log.e("AuthApiImpl", "HTTP error ${response.status}: $err")
                    ApiResult.UnknownError("HTTP error ${response.status.value}: $err")
                }
            }
        } catch (e: Exception) {
            ApiResult.UnknownError("Error retrieving profile: ${e.localizedMessage}")
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
                    ApiResult.Success(Unit, "Agency loaded and saved successfully")
                }
                HttpStatusCode.NotFound -> {
                    val msg = "No agency found for userId=$userId"
                    println(msg)
                    ApiResult.UnknownError(msg)
                }
                else -> {
                    val err = response.bodyAsText()
                    println("HTTP error ${response.status.value}: $err")
                    ApiResult.UnknownError("HTTP error ${response.status.value}: $err")
                }
            }
        } catch (e: ClientRequestException) {
            ApiResult.UnknownError("HTTP error ${e.response.status.value}: ${e.response.bodyAsText()}")
        } catch (e: Exception) {
            ApiResult.UnknownError("Error retrieving agency: ${e.localizedMessage}")
        }
    }

    override suspend fun fetchState(): ApiResult<String> {
        return try {
            Log.d("AuthRepository", "Starting FetchState")

            val response: HttpResponse = httpClient.get("$baseURL/generate-state")
            val state = response.bodyAsText()

            return if (response.status == HttpStatusCode.OK && state.isNotEmpty()) {
                Log.d("AuthRepository", "State successfully generated: $state")
                ApiResult.Success(state)
            } else {
                val err = response.bodyAsText()
                Log.e("AuthRepository", "HTTP error ${response.status}: $err")
                ApiResult.UnknownError("HTTP error ${response.status.value}: $err")
            }
        } catch (e: ResponseException) {
            Log.e("AuthRepository", "HTTP error: ${e.response.status}", e)
            when (e.response.status) {
                HttpStatusCode.BadRequest -> ApiResult.Unauthorized("Invalid request")
                HttpStatusCode.Unauthorized -> ApiResult.Unauthorized("Access denied")
                else -> ApiResult.UnknownError("HTTP error ${e.response.status.value}")
            }
        } catch (e: JSONException) {
            Log.e("AuthRepository", "Error parsing JSON response", e)
            ApiResult.UnknownError("Invalid JSON response")
        } catch (e: Exception) {
            Log.e("AuthRepository", "General error during FetchState", e)
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

    override suspend fun githubOauth(code: String?, state: String?): ApiResult<Unit> {
        if (code.isNullOrBlank() || state.isNullOrBlank()) {
            Log.e("OAuth", "Missing parameters: code or state are null or empty")
            return ApiResult.UnknownError("Code or state cannot be null or empty")
        }

        return try {
            Log.d("OAuth", "Sending code exchange request: code=$code, state=$state")

            val response: HttpResponse = httpClient.post("$baseURL/auth/github") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(mapOf("code" to code, "state" to state))
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val tokenResponse: TokenResponse = response.body()
                    Log.d("AuthApiImpl", "Response received: token = ${tokenResponse.token}")

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
                    Log.e("OAuth", "Authentication failed: $err")
                    ApiResult.Unauthorized("Authorization error: $err")
                }
                else -> {
                    val err = response.bodyAsText()
                    Log.e("OAuth", "HTTP error ${response.status}: $err")
                    ApiResult.UnknownError("HTTP error ${response.status.value}: $err")
                }
            }
        } catch (e: ResponseException) {
            Log.e("OAuth", "HTTP error: ${e.response.status}", e)
            when (e.response.status) {
                HttpStatusCode.BadRequest -> ApiResult.Unauthorized("Invalid request")
                HttpStatusCode.Unauthorized -> ApiResult.Unauthorized("Access denied")
                else -> ApiResult.UnknownError("HTTP error ${e.response.status.value}")
            }
        } catch (e: Exception) {
            Log.e("OAuth", "Generic error during GitHub OAuth: ${e.localizedMessage}", e)
            ApiResult.UnknownError("Generic error: ${e.localizedMessage}")
        }
    }

}
