package com.example.ingsw_24_25_dietiestates25.ui.navigation

import com.example.ingsw_24_25_dietiestates25.ui.authUI.AuthViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.ui.HomeScreen
import com.example.ingsw_24_25_dietiestates25.ui.authUI.AuthScreen
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.ProfileScreen
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.ProfileViewModel

//PUSHINNNNNNGGGG
enum class Screen {
    HOME,
    AUTH,
    PROFILE
}
sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object Auth : NavigationItem(Screen.AUTH.name)
    object Profile : NavigationItem(Screen.PROFILE.name)
}


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationItem.Auth.route
) {

    val authViewModel: AuthViewModel = hiltViewModel()
    val profileViewModel : ProfileViewModel = hiltViewModel()

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
                navController = navController
            )
        }




        composable(NavigationItem.Home.route) {
            ProfileScreen(
//                pm = profileViewModel,
//                navController = navController
            )
        }

    }
}

