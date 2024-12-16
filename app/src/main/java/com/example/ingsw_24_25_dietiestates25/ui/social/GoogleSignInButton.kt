package com.example.ingsw_24_25_dietiestates25.ui.social


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.security.MessageDigest
import java.util.UUID


@Composable
fun GoogleSignInButton(
    context: Context = LocalContext.current
) {
    val coroutineScope = rememberCoroutineScope()
    val TAG = "GoogleSignIn"

    OutlinedButton(
        onClick = {
            val credentialManager = CredentialManager.create(context)

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

                    Log.i(TAG, googleIdToken)

                    val idToken = googleIdTokenCredential.idToken
                    val segments = idToken.split(".")
                    val payloadAsByteArray =
                        android.util.Base64.decode(segments[1], android.util.Base64.NO_PADDING)
                    val payloadInJson = JSONObject(String(payloadAsByteArray, Charsets.UTF_8))
                    val uniqueIdentifier = payloadInJson.getString("email")

                    Toast.makeText(
                        context,
                        payloadInJson.getString("email") + " " + payloadInJson.getString("name"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: androidx.credentials.exceptions.GetCredentialException) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                } catch (e: GoogleIdTokenParsingException) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        },
        modifier = Modifier
            .height(40.dp) // Altezza del pulsante

    ) {
        Text(
            text = "Google", // Nome della piattaforma
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) // Stile del testo
        )
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


