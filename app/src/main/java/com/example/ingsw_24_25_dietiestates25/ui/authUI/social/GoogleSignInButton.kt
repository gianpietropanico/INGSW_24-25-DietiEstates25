package com.example.ingsw_24_25_dietiestates25.ui.authUI.social


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.authUI.AuthViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.security.MessageDigest
import java.util.UUID


@Composable
fun GoogleSignInButton(
    context: Context = LocalContext.current,
    am: AuthViewModel,
) {
    var isLoading by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val tag = "GoogleSignIn"

    Box(
        modifier = Modifier
            .height(60.dp) // Altezza del pulsante
            .clickable(
                enabled = !isLoading
            ) {
                isLoading = true
                resultMessage = null // Reset error messages
                val credentialManager = CredentialManager.create(context)
                var uniqueIdentifier : String? = null
                var nameIdentifier : String? = null

                val rawNonce = UUID.randomUUID().toString()
                val bytes = rawNonce.toByteArray()
                val md = MessageDigest.getInstance("SHA-256")
                val digest = md.digest(bytes)
                val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

                val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("747472826480-gu5faa67ev4uocejm52tscmo1bjecq0h.apps.googleusercontent.com")
                    .setNonce(hashedNonce)
                    .build()

                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                coroutineScope.launch {
                    try {
                        val result = credentialManager.getCredential(
                            context = context,
                            request = request
                        )

                        val credential = result.credential

                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)

                        val googleIdToken = googleIdTokenCredential.idToken

                        Log.i(tag, googleIdToken)

                        val idToken = googleIdTokenCredential.idToken
                        val segments = idToken.split(".")
                        val payloadAsByteArray =
                            android.util.Base64.decode(segments[1], android.util.Base64.NO_PADDING)
                        val payloadInJson = JSONObject(String(payloadAsByteArray, Charsets.UTF_8))
                        uniqueIdentifier = payloadInJson.getString("email")
                        nameIdentifier = payloadInJson.getString("name")

                        resultMessage = "Authentication successful! " + payloadInJson.getString("email") + " " + payloadInJson.getString("name")

                    } catch (e: androidx.credentials.exceptions.GetCredentialException) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        Log.d("GOGGLE COROUTINE","${e.message}")
                        resultMessage = e.message
                    } catch (e: GoogleIdTokenParsingException) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        Log.d("GOGGLE COROUTINE","${e.message}")
                    }finally {
                        isLoading = false
                        am.authWithThirdParty(uniqueIdentifier, nameIdentifier )
                    }
                }
            }
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
        } else {
            Icon(
                painter = painterResource(id = R.drawable.google_icon), // Icona di Google
                contentDescription = "Google Icon", // Descrizione per l'accessibilità
                modifier = Modifier.size(60.dp), // Dimensione dell'icona
                tint = Color.Unspecified // Mantieni i colori originali dell'icona
            )
        }
    }

}


/*
  PAYLOAD GOOGLE{
      "iss": "https://accounts.google.com",
      "azp": "YOUR_CLIENT_ID",
      "aud": "YOUR_CLIENT_ID",
      "sub": "123456789012345678901",
      "email": "user@example.com",
      "email_verified": true,
      "name": "John Doe",
      "picture": "https://lh3.googleusercontent.com/a-/AOh14Gj...",
      "given_name": "John",
      "family_name": "Doe",
      "locale": "en",
      "iat": 1614036036,
      "exp": 1614039636
  }
 */

