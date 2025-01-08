package com.example.ingsw_24_25_dietiestates25.data.auth
import android.content.SharedPreferences
import android.util.Log
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val prefs: SharedPreferences
): AuthRepository {
    override suspend fun signUp(email: String, password: String): AuthResult<Unit> {
        return try{
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

            prefs.edit()
                .putString("jwt", response.token)
                .apply()

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
        return try{
            val token = prefs.getString("jwt", null)?: return AuthResult.Unauthorized()
            api.authenticate("bearer $token")
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

    override suspend fun fetchGitHubAccessToken(code: String): AuthResult<String> {
        return try {
            val response = api.getGitHubAccessToken(code) // Passa il codice
            Log.d("AuthRepository", "Token GitHub ricevuto: ${response.token}")

            prefs.edit()
                .putString("github_token", response.token)
                .apply()

            AuthResult.Authorized(response.token)
        } catch (e: ResponseException) {
            Log.e("AuthRepository", "Errore HTTP durante la richiesta del token GitHub", e)
            AuthResult.UnknownError()
        } catch (e: Exception) {
            Log.e("AuthRepository", "Errore generale durante la richiesta del token GitHub", e)
            AuthResult.UnknownError()
        }
    }



}