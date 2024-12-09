package com.example.ingsw_24_25_dietiestates25

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthApiImpl
import com.example.ingsw_24_25_dietiestates25.data.auth.AuthRepositoryImpl
import com.example.ingsw_24_25_dietiestates25.data.network.NetworkClient
import com.example.ingsw_24_25_dietiestates25.ui.AuthViewModel
import com.example.ingsw_24_25_dietiestates25.ui.AuthViewModelFactory
import com.example.ingsw_24_25_dietiestates25.ui.AuthenticateApp
import com.example.ingsw_24_25_dietiestates25.ui.HomeApp


enum class AuthScreen(){
    Authenticate,
    Home
}

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
) {

    val context = LocalContext.current

    val authRepository = AuthRepositoryImpl(
        api = AuthApiImpl(NetworkClient.httpClient),
        prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    )

    val factory = AuthViewModelFactory(authRepository)

    val viewModel: AuthViewModel = viewModel(factory = factory)

    // NavHost per gestire la navigazione tra le schermate
    NavHost(
        navController = navController,
        startDestination = AuthScreen.Authenticate.name // Schermata iniziale
    ) {
        // Schermata di Login
        composable(route = AuthScreen.Authenticate.name) {
            AuthenticateApp(
                /* TODO LEVARE IL VIEWMODEL E PASSARE SOLO IL TOKEN DI VERIFICA CON LE NECESSARIE CREDENZIALI*/
                viewModel = viewModel,
                onAuthenticateClicked = {
                    // Naviga alla schermata di autenticazione
                    navController.navigate(AuthScreen.Home.name)
                }
            )
        }

        // Schermata di autenticazione (o successiva)
        composable(route = AuthScreen.Home.name) {
            // Logica successiva alla schermata di Login
            HomeApp(
                viewModel = viewModel,
                onLogoutClicked = {
                    navController.navigate(AuthScreen.Authenticate.name) {
                        popUpTo(AuthScreen.Home.name) { inclusive = true }
                    }
                }
            )
        }
    }
}

