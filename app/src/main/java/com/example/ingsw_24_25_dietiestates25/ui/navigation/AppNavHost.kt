package com.example.ingsw_24_25_dietiestates25.ui.navigation

import android.util.Log
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
import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.SysAdminAgencyScreen
import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.SysAdminFormSuppScreen
import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.SysAdminHomeScreen
import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.SysAdminInboxScreen
import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.SysAdminSupportsScreen
import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.SysAdminViewModel


enum class Screen {
    HOME,
    WELCOME,
    PROFILE,
    SIGNIN,
    SIGNUP,
    AGENCYSIGNIN,
    PROPERTYLISTING,
    PROFILEDETAILS,
    PROFILEEDITDETAILS,
    SYSADMINAGENCY,
    INBOX,
    SYSADMINSUPP,
    SYSFORMADMINSUPP
}
sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object SignIn : NavigationItem(Screen.SIGNIN.name)
    object SignUp : NavigationItem(Screen.SIGNUP.name)
    object Welcome : NavigationItem(Screen.WELCOME.name)
    object Profile : NavigationItem(Screen.PROFILE.name)
    object AgencySignIn : NavigationItem(Screen.AGENCYSIGNIN.name)
    object PropertyListing : NavigationItem(Screen.PROPERTYLISTING.name)
    object ProfileDetails : NavigationItem(Screen.PROFILEDETAILS.name)
    object ProfileEditDetails : NavigationItem(Screen.PROFILEEDITDETAILS.name)
    object Inbox : NavigationItem(Screen.INBOX.name)
    object SysAdminAgency : NavigationItem(Screen.SYSADMINAGENCY.name)
    object SysAdminSupp : NavigationItem(Screen.SYSADMINSUPP.name)
    object SysFormAdminSupp : NavigationItem(Screen.SYSFORMADMINSUPP.name)
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
    val systemAdminViewModel : SysAdminViewModel = hiltViewModel()

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

        composable(NavigationItem.Home.route){

            val userRole = authViewModel.getUserRole()

            when (userRole){
                "SUPER_ADMIN" -> {
                    SysAdminHomeScreen(
                        navController = navController,
                        sysAdminVm = systemAdminViewModel
                    )
                }
                "SUPPORT_ADMIN" -> {
                    SysAdminHomeScreen(
                        navController = navController,
                        sysAdminVm = systemAdminViewModel
                    )
                }
                "AGENCY_ADMIN" -> {
                    HomeScreen(
                        navController = navController
                    )
                }
                "AGENT_USER" ->{
                    AgentHomeScreen(
                        plm = propertyListingViewModel,
                        navController = navController
                    )
                }
                else -> {
                    HomeScreen(
                        navController = navController
                    )
                }
            }

        }

        composable(NavigationItem.Inbox.route){

            val userRole = authViewModel.getUserRole()

            when (userRole){
                "SUPER_ADMIN" -> {
                    SysAdminInboxScreen(
                        navController = navController,
                        sysAdminVm = systemAdminViewModel
                    )
                }
                "SUPPORT_ADMIN" -> {
                    /*TODO*/
                }
                "AGENCY_ADMIN" -> {
                    /*TODO*/
                }
                "AGENT_USER" ->{
                    /*TODO*/
                }
                else -> {
                    /*TODO*/
                }
            }

        }

        composable(NavigationItem.SysAdminSupp.route){
            SysAdminSupportsScreen(
                sysAdminVm = systemAdminViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.SysFormAdminSupp.route) {
            Log.d("NAV", "Entrato in SysAdminFormSuppScreen")
            SysAdminFormSuppScreen(
                sysAdminVm = systemAdminViewModel,
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

        composable(NavigationItem.ProfileDetails.route) {
            ProfileDetailsScreen(
                navController = navController,
                pm = profileViewModel
            )
        }

        composable(NavigationItem.SysAdminAgency.route) {
            SysAdminAgencyScreen(
                navController = navController,
                sysAdminVm = systemAdminViewModel
            )
        }

    }
}

