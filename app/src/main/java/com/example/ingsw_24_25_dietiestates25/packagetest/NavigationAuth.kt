package com.example.ingsw_24_25_dietiestates25.packagetest

import android.view.View
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationAuth(
    navigationHandler: NavigationHandler = DefaultNavigationHandler()
){
    val navController = rememberNavController()

    //NESTED NAVIGATION GRAPH ,HELL YEAH
    NavHost(navController = navController, startDestination = "auth") {
        navigation(
            startDestination = ScreenRoutes.AuthScreen.route,
            route = ScreenRoutes.SecretScreen.route
        ) {
            composable(ScreenRoutes.SecretScreen.route) {
                val viewModel = it.sharedViewModel<MainViewModel>(navController)

                Button(onClick = {
                    navController.navigate(ScreenRoutes.SecretScreen.route) {
                        popUpTo(ScreenRoutes.AuthScreen.route) {
                            inclusive = true
                        }
                    }
                }) {
                    Text("Vai alla schermata segreta")
                }
            }
        }

    }

}

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {

    val navGraphRoute = destination.parent?.route ?: return viewModel() // se uso dagger hilt dovrebbbe restituire hiltedviewmodel
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return viewModel(parentEntry)
}

/*
@Composable
fun NavigationAuth(){
    val navController = rememberNavController()

    //NESTED NAVIGATION GRAPH ,HELL YEAH
    NavHost(navController = navController, startDestination = "auth") {
        navigation(
            startDestination = "login",
            route = "auth"
        ){
            composable("auth") {
                val viewModel = it.sharedViewModel<MainViewModel>(navController)

                Button(onClick = {
                    navController.navigate("secretScreen"){
                        popUpTo("auth"){
                            inclusive = true//L'UTENTE NON PUO PIU ANDARE INDIETRO
                        }
                    }
                }){
                    Frame.Text("Vai alla schermata segreta")
                }

            }

            /*POSSO CREARE TUTTI LE ROUTE POSSIBILI
            *E FARANNO TUTTI RIFERIMENTO AL NAVIGATION DI QUESTA ROOT
            * UNA VOLTA CHE NON NE ABBIAMO PIU BISOGNO VIENE POP FUORI
            * QUINDI LA VIEW MODEL SARà RIPULITA DALLA MEMORIA
            *   composable("register"){
            *
            *   }
            *
            *   composable("sasso"){
            *
            *   }
            */
        }
        /*
        * POSSO CREARE ANCHE QUI ALTRE NESTED NAVGRAOPH
        *   navigation(
        *   startDestination = "calendar_overview",
        *       route = "calendar"
        *   ){
        *   composable("calendar_overview") {
        *       val viewModel = it.sharedViewModel<SampleViewModel>(navController)
        *   }
        * */
    }

}

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {

    val navGraphRoute = destination.parent?.route ?: return viewModel() // se uso dagger hilt dovrebbbe restituire hiltedviewmodel
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return viewModel(parentEntry)
}*/