package com.example.ingsw_24_25_dietiestates25.data.auth
import android.content.SharedPreferences
import android.net.http.HttpException
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
        return try{
            val response = api.signIn(
                request = AuthRequest(
                    email = email,
                    password = password
                )
            )
            prefs.edit()
                .putString("jwt",response.token)
                .apply()

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

}