package com.example.ingsw_24_25_dietiestates25.ui.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.HomeViewModel
import com.example.ingsw_24_25_dietiestates25.ResultsViewModel
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Role
import com.example.ingsw_24_25_dietiestates25.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.ui.HomeScreen
import com.example.ingsw_24_25_dietiestates25.ui.ResultsScreen
import com.example.ingsw_24_25_dietiestates25.ui.authUI.*
import com.example.ingsw_24_25_dietiestates25.ui.agentUI.AgentHomeScreen
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.*
import com.example.ingsw_24_25_dietiestates25.ui.propertyListingUI.AddPropertyListingScreen
import com.example.ingsw_24_25_dietiestates25.ui.propertyListingUI.PropertyListingViewModel
import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.*

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
    SYSFORMADMINSUPP,
    RESULTS
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
    object Results : NavigationItem(Screen.RESULTS.name)
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationItem.Results.route
) {

    val authViewModel: AuthViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val propertyListingViewModel: PropertyListingViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val systemAdminViewModel: SysAdminViewModel = hiltViewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {

        composable(NavigationItem.Welcome.route) {
            WelcomeScreen(
                am = authViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.Results.route) {

            val user = User(
                id = "fehyfhiwp",
                username = "cacca",
                name = "gianni",
                surname = "peppe",
                email = "gian@gmail,com",
                role = Role.LOCAL_USER,
                profilePicture = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/w8AAn8B9pye4QAAAABJRU5ErkJggg=="
            )

            val sessionManager: UserSessionManager = UserSessionManager()
            sessionManager.saveUser(user, "ciao")

            when (user.role.name) {
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
                    AgentHomeScreen(
                        plm = propertyListingViewModel,
                        navController = navController
                    )
                }

                "AGENT_USER" -> {
                    AgentHomeScreen(
                        plm = propertyListingViewModel,
                        navController = navController
                    )
                }

                else -> {
                    HomeScreen(
                        hm = homeViewModel,
                        navController = navController

                    )
                }
            }
        }

        composable(NavigationItem.Inbox.route) {
            val userRole = authViewModel.getUserRole()

            when (userRole) {
                "SUPER_ADMIN" -> {
                    SysAdminInboxScreen(
                        navController = navController,
                        sysAdminVm = systemAdminViewModel
                    )
                }

                "SUPPORT_ADMIN" -> { /*TODO*/ }
                "AGENCY_ADMIN" -> { /*TODO*/ }
                "AGENT_USER" -> { /*TODO*/ }
                else -> { /*TODO*/ }
            }
        }

        composable(NavigationItem.SysAdminSupp.route) {
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

        composable(NavigationItem.AgencySignIn.route) {
            AgencySignInScreen(
                am = authViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.SignIn.route) {
            SignInScreen(
                am = authViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.ProfileEditDetails.route) {
            ProfileEditDetailsScreen(
                pm = profileViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.SignUp.route) {
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

        composable(NavigationItem.PropertyListing.route) {
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


        composable(
            route = "${NavigationItem.Results.route}?type={type}&location={location}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("location") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "Rent"
            val location = backStackEntry.arguments?.getString("location") ?: ""

            val resultsViewModel: ResultsViewModel = hiltViewModel(backStackEntry) // messo qui e non sopra con gli altri perche
            //dipende da altri parametri e si deve resettare ogni volta

            ResultsScreen(
                navController = navController,
                type = type,
                location = location,
                rm = resultsViewModel
            )
        }
    }
}