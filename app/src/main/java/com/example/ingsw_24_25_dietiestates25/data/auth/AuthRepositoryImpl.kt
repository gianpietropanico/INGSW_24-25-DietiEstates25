package com.example.ingsw_24_25_dietiestates25.data.auth
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.example.ingsw_24_25_dietiestates25.data.model.User
import com.example.ingsw_24_25_dietiestates25.data.network.NetworkClient.httpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.json.JSONObject


class AuthRepositoryImpl(
    private val api: AuthApi,
    private val prefs: SharedPreferences
): AuthRepository {
    override suspend fun signUp(email: String, password: String): AuthResult<Unit> {
        return try {
            api.signUp(
                request = AuthRequest(
                    email = email,
                    password = password
                )
            )
            signIn(email, password)

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

    override suspend fun signIn(email: String, password: String): AuthResult<Unit> {
        return try {
            Log.d("AuthRepository", "Inizio SignIn per: $email")
            val response = api.signIn(
                request = AuthRequest(
                    email = email,
                    password = password
                )
            )
            Log.d("AuthRepository", "SignIn API risposta token: ${response.token}")

            saveTokenAndUser(response.token) // Salva token e payload

            AuthResult.Authorized()

        } catch (e: ResponseException) {
            Log.e("AuthRepository", "Errore HTTP: ${e.response.status}", e)
            if (e.response.status == HttpStatusCode.Unauthorized) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Errore generale durante SignIn", e)
            AuthResult.UnknownError()
        }
    }


    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            api.authenticate("Bearer $token")
            AuthResult.Authorized()

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
            // Rimuovi il token JWT dalle SharedPreferences
            prefs.edit()
                .remove("jwt")
                .apply()

            // (Facoltativo) Chiamata al server per invalidare il token, se supportata
            // api.logout()

            Log.d("AuthRepository", "Logout completato con successo")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Errore durante il logout", e)
        }
    }

    private fun saveTokenAndUser(token: String) {
        // Salva il token nelle SharedPreferences
        prefs.edit()
            .putString("jwt", token)
            .apply()

        // Decodifica il payload dal token JWT
        val payload = decodeJwtPayload(token)
        payload?.let {
            val email = it.optString("email", null)
            val username = it.optString("username", null)

            // Salva i dettagli utente nelle SharedPreferences (opzionale)
            prefs.edit()
                .putString("user_email", email)
                .putString("user_username", username)
                .apply()
        }
    }

    override suspend fun fetchState(): AuthResult<String> {
        return try {
            Log.d("AuthRepository", "Inizio FetchState")
            val state = api.fetchStateKtor() // Chiamata diretta all'API per ottenere lo stato

            Log.d("AuthRepository", "Stato generato con successo: $state")

            AuthResult.Authorized(state) // Incapsula lo stato nel risultato
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
    override suspend fun notifyServer(code: String?, state: String?): AuthResult<User> {
        return try {
            Log.d("AuthRepository", "Inizio notify con code=$code e state=$state")

            val user = api.notifyServer(code, state) // Chiamata diretta all'API per ottenere l'utente

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






    private fun decodeJwtPayload(token: String): JSONObject? {
        return try {
            val parts = token.split(".")
            if (parts.size == 3) {
                val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
                JSONObject(payload)
            } else null
        } catch (e: Exception) {
            null
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

