package com.example.ingsw_24_25_dietiestates25.packagetest

import androidx.navigation.NavController

interface NavigationHandler {
    fun navigateToSecretScreen(navController: NavController)
}

class DefaultNavigationHandler : NavigationHandler {
    override fun navigateToSecretScreen(navController: NavController) {
        navController.navigate("secretScreen") {
            popUpTo("auth") { inclusive = true }
        }
    }
}
