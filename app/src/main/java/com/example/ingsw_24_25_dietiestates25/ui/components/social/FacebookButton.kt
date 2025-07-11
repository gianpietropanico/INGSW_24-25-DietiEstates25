package com.example.ingsw_24_25_dietiestates25.ui.components.social

import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.CallbackManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.ingsw_24_25_dietiestates25.R
import com.facebook.GraphRequest

/*TEST FUNZIONI E VARIABILI PER CAPIRE SE L'ACCESSTOKEN DELL'UTENTE è VALIDO
  POTREBBERO ESSERE IMPLEMENTATE IN FUTURO
    var userEmail by remember { mutableStateOf("") }
    var isLoggedIn by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }

    // Controlla lo stato di accesso all'avvio
    LaunchedEffect(Unit) {
        val accessToken = AccessToken.getCurrentAccessToken()
        isLoggedIn = accessToken != null && !accessToken.isExpired
        Log.d(
            "FacebookLogin",
            "AccessToken: ${accessToken?.token ?: "No token"}, isLoggedIn: $isLoggedIn"
        )

        if (isLoggedIn) {
            val profile = Profile.getCurrentProfile()
            userName = profile?.name ?: ""
            Log.d("FacebookLogin", "User Profile Name: $userName")
        }
    }
*/


@Composable
fun FacebookLoginButton() {

    var isLoading by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(null) }

    val callbackManager = CallbackManager.Factory.create()
    val loginManager = LoginManager.getInstance()
    val context = LocalContext.current

    // Registra le callback per il login
    loginManager.registerCallback(
        callbackManager,
        object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                Log.d("FacebookLogin", "Login cancelled.")
                isLoading = false
                resultMessage = "Operazione Fallita"
            }

            override fun onError(error: FacebookException) {
                Log.e("FacebookLogin", "Error during login: ${error.message}")
                resultMessage = "Operazione Fallita, errore durante il login"
                isLoading = false
                /*TODO NOTIFICARE CHE L'UTENTE NON è STATO LOGGATO */
            }

            override fun onSuccess(result: LoginResult) {
                GraphRequest.newMeRequest(result?.accessToken) { obj, _ ->
                    val email = obj?.getString("email") ?: "Email non disponibile"
                    val name = obj?.getString("name") ?: "Nome non disponibile"


                    /*TODO in questo caso bisogna chiamare il server per verificare
                        i dati nel db nel caso salvarli con eventuali resultmessage cambiati
                     */
                    isLoading = false
                    resultMessage = "Operazione riuscita" +" Email: $email, Nome: $name"

                    Log.d("FacebookLogin", "Email: $email, Nome: $name")
                    Log.d("TEST TOAST","MESSAGE1")
                    //myToastMessage(context, resultMessage)
                    Log.d("TEST TOAST","MESSAGE2")
                }.apply {
                    parameters = Bundle().apply {
                        putString("fields", "id,name,email")
                    }
                    executeAsync()
                }
            }


        }
    )
    // Pulsante di login
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(60.dp) // Dimensione totale del pulsante
            .clickable(
                enabled = !isLoading
            ){
                Log.d("FacebookAuth", "Bottone premuto: Avvio login")
                isLoading = true
                resultMessage = null // Reset error messages
                loginManager.logIn(
                    context as ActivityResultRegistryOwner,
                    callbackManager,
                    listOf("email", "public_profile") // Permessi richiesti

                )


            }
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
        } else {
            Icon(
                painter = painterResource(id = R.drawable.facebook_icon), // Icona di Facebook
                contentDescription = "Facebook Icon", // Descrizione per l'accessibilità
                modifier = Modifier.fillMaxSize(), // L'icona occupa tutto lo spazio disponibile
                tint = Color.Unspecified // Mantieni i colori originali dell'icona
            )
        }
    }
}