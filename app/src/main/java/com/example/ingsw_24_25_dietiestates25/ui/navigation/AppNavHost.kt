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
import com.example.ingsw_24_25_dietiestates25.ui.authUI.AgencySignInScreen
import com.example.ingsw_24_25_dietiestates25.ui.authUI.SignInScreen
import com.example.ingsw_24_25_dietiestates25.ui.authUI.SignUpScreen
import com.example.ingsw_24_25_dietiestates25.ui.authUI.WelcomeScreen
import com.example.ingsw_24_25_dietiestates25.ui.agentUI.AgentHomeScreen
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.ProfileDetailsScreen
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.ProfileEditDetailsScreen
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.ProfileScreen
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.ProfileViewModel
import com.example.ingsw_24_25_dietiestates25.ui.propertyListingUI.AddPropertyListingScreen
import com.example.ingsw_24_25_dietiestates25.ui.propertyListingUI.PropertyListingViewModel


enum class Screen {
    HOME,
    WELCOME,
    PROFILE,
    SIGNIN,
    SIGNUP,
    AGENCYSIGNIN,
    PROPERTYLISTING,
    AGENTHOME,
    PROFILEDETAILS,
    PROFILEEDITDETAILS
}
sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object SignIn : NavigationItem(Screen.SIGNIN.name)
    object SignUp : NavigationItem(Screen.SIGNUP.name)
    object Welcome : NavigationItem(Screen.WELCOME.name)
    object Profile : NavigationItem(Screen.PROFILE.name)
    object AgencySignIn : NavigationItem(Screen.AGENCYSIGNIN.name)
    object PropertyListing : NavigationItem(Screen.PROPERTYLISTING.name)
    object AgentHome : NavigationItem(Screen.AGENTHOME.name)
    object ProfileDetails : NavigationItem(Screen.PROFILEDETAILS.name)
    object ProfileEditDetails : NavigationItem(Screen.PROFILEEDITDETAILS.name)
}


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationItem.Welcome.route
) {

    val authViewModel: AuthViewModel = hiltViewModel()
    val profileViewModel : ProfileViewModel = hiltViewModel()
    val propertyListingViewModel : PropertyListingViewModel = hiltViewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {

        composable(NavigationItem.Welcome.route){
            WelcomeScreen(
                am = authViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.AgencySignIn.route){
            AgencySignInScreen(
                am = authViewModel,
                navController = navController
            )
        }


        composable(NavigationItem.SignIn.route){
            SignInScreen(
                am = authViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.ProfileEditDetails.route){
            ProfileEditDetailsScreen(
                pm = profileViewModel,
                navController = navController
            )
        }


        composable(NavigationItem.SignUp.route){
            SignUpScreen(
                am = authViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.Home.route) {
            HomeScreen(
                navController = navController
            )
        }

        composable(NavigationItem.Profile.route) {
            ProfileScreen(
                navController = navController,
                pm = profileViewModel
            )
        }

        composable(NavigationItem.PropertyListing.route){
            AddPropertyListingScreen(
                navController = navController,
                plm = propertyListingViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavigationItem.AgentHome.route) {
            AgentHomeScreen(
                plm = propertyListingViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.ProfileDetails.route) {
            ProfileDetailsScreen(
                navController = navController,
                pm = profileViewModel
            )
        }



    }
}

