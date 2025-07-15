package com.example.ingsw_24_25_dietiestates25.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import com.example.ingsw_24_25_dietiestates25.ui.authenticate.AuthViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.ingsw_24_25_dietiestates25.ui.HomeScreen
import com.example.ingsw_24_25_dietiestates25.ui.authenticate.AuthScreen
import kotlinx.coroutines.flow.map


enum class Screen {
    HOME,
    AUTH
}
sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object Auth : NavigationItem(Screen.AUTH.name)


}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationItem.Auth.route
) {
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {

        composable(NavigationItem.Auth.route){
            AuthScreen(
                am = authViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.Home.route) {
            HomeScreen(
                am = authViewModel,
                navController = navController)
        }

        // — nuova route deep-link per GitHub OAuth —
        composable(
            route = "github/callback?code={code}&state={state}",
            arguments = listOf(
                navArgument("code") { type = NavType.StringType },
                navArgument("state") { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = "dietiestates25://callback/github?code={code}&state={state}" }
            )
        ) { backStackEntry ->
            val code = backStackEntry.arguments!!.getString("code")!!
            val state = backStackEntry.arguments!!.getString("state")!!

            // 1) Appena entri, chiami la callback
            LaunchedEffect(code, state) {
                authViewModel.handleCallback(code, state)
            }

            // 2) Quando l'autenticazione è andata a buon fine, navighi in Home
            val isAuthenticated by authViewModel.authState.map { it.isAuthenticated }.collectAsState(false)
            LaunchedEffect(isAuthenticated) {
                if (isAuthenticated == true) {
                    navController.navigate(NavigationItem.Home.route)
                }
            }

            // UI di caricamento / eventuale errore
            val uiState by authViewModel.authState.collectAsState()
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else if (uiState.errorMessage != null) {
                    Text("Errore: ${uiState.errorMessage}")
                }
            }
        }



    }
}

