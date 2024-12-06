package com.example.ingsw_24_25_dietiestates25.packagetest


sealed class ScreenRoutes(val route: String) {
    //object Login : ScreenRoutes("login")
    object SecretScreen : ScreenRoutes("secretScreen")
    object AuthScreen : ScreenRoutes("auth")
}
