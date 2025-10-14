package com.example.ingsw_24_25_dietiestates25.ui.navigation


import com.example.ingsw_24_25_dietiestates25.ui.authUI.AuthViewModel
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
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.ui.HomeScreen
import com.example.ingsw_24_25_dietiestates25.ui.ResultsScreen
import com.example.ingsw_24_25_dietiestates25.ui.authUI.*
import com.example.ingsw_24_25_dietiestates25.ui.agentUI.AgentHomeScreen

import com.example.ingsw_24_25_dietiestates25.ui.agentUI.AgentViewModel
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.ProfileDetailsScreen
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.ProfileEditDetailsScreen
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.ProfileScreen
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.ProfileViewModel

import com.example.ingsw_24_25_dietiestates25.ui.agentUI.AgencySettingsScreen
import com.example.ingsw_24_25_dietiestates25.ui.appointmentUI.AppointmentChatScreen
import com.example.ingsw_24_25_dietiestates25.ui.appointmentUI.AppointmentViewModel
import com.example.ingsw_24_25_dietiestates25.ui.appointmentUI.BookAppointmentScreen
import com.example.ingsw_24_25_dietiestates25.ui.appointmentUI.CheckAllAppointmentsScreen
import com.example.ingsw_24_25_dietiestates25.ui.appointmentUI.CheckListingAppointmentScreen
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.AddPropertyListingScreen
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.ListingDetailScreen
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.ListingScreen
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.ListingViewModel
import com.example.ingsw_24_25_dietiestates25.ui.offerUI.InboxScreen
import com.example.ingsw_24_25_dietiestates25.ui.offerUI.InboxViewModel
import com.example.ingsw_24_25_dietiestates25.ui.offerUI.MakeOfferScreen
import com.example.ingsw_24_25_dietiestates25.ui.offerUI.OfferChatScreen
import com.example.ingsw_24_25_dietiestates25.ui.offerUI.InboxScreen
import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherViewModel


import com.example.ingsw_24_25_dietiestates25.ui.sendEmailFormUI.MailerSenderViewModel
import com.example.ingsw_24_25_dietiestates25.ui.sendEmailFormUI.SendEmailFormScreen
import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.SysAdminAgencyScreen

import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.SysAdminHomeScreen

import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.SysAdminSupportsScreen
import com.example.ingsw_24_25_dietiestates25.ui.systemAdminUI.SysAdminViewModel

import com.example.ingsw_24_25_dietiestates25.ui.profileUI.*


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
    RESULTS,
    FORMUSER,
    AGENTLISTINGS,
    AGENTAGENCY,
    USERACTIVITIES,
    ADDPROPERTYLISTING,
    MAKEOFFER,
    OFFERCHAT,
    LISTINGDETAIL,
    APPOINTMENTCHAT,
    BOOKAPPOINTMENT,
    CHECKAPPOINTMENT,

    CHECKALLAPPOINTMENT
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
    object Results : NavigationItem(Screen.RESULTS.name)
    object FormUser : NavigationItem(Screen.FORMUSER.name)
    object AgentListings : NavigationItem(Screen.AGENTLISTINGS.name)
    object AgentAgency : NavigationItem(Screen.AGENTAGENCY.name)
    object UserActivities : NavigationItem(Screen.USERACTIVITIES.name)
    object AddPropertyListings : NavigationItem(Screen.ADDPROPERTYLISTING.name)
    object MakeOffer : NavigationItem(Screen.MAKEOFFER.name)
    object OfferChat : NavigationItem(Screen.OFFERCHAT.name)

    object ListingDetail : NavigationItem(Screen.LISTINGDETAIL.name)
    object AppointmentChat : NavigationItem(Screen.APPOINTMENTCHAT.name)
    object InboxScreen : NavigationItem(Screen.INBOX.name)
    object BookAppointment : NavigationItem(Screen.BOOKAPPOINTMENT.name)
    object CheckListingAppointment : NavigationItem(Screen.CHECKAPPOINTMENT.name)

    object CheckAllAppointments : NavigationItem(Screen.CHECKALLAPPOINTMENT.name)
}


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationItem.Welcome.route
) {

    val authViewModel: AuthViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val profileViewModel : ProfileViewModel = hiltViewModel()
    val systemAdminViewModel : SysAdminViewModel = hiltViewModel()
    val agentViewModel : AgentViewModel = hiltViewModel()
    val mailerSenderViewModel : MailerSenderViewModel = hiltViewModel()
    val listingViewModel : ListingViewModel = hiltViewModel()
    val inboxViewModel: InboxViewModel = hiltViewModel()
    val appointmentViewModel : AppointmentViewModel = hiltViewModel()
    val weatherViewModel : WeatherViewModel = hiltViewModel()

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

        composable(NavigationItem.UserActivities.route){
            ActivitiesScreen(
                profileVm = profileViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.AddPropertyListings.route){
            AddPropertyListingScreen(
                listingVm = listingViewModel,
                navController = navController,

            )
        }


        composable(NavigationItem.ListingDetail.route){

            ListingDetailScreen(
                listingVm = listingViewModel,
                navController = navController,
                inboxVm = inboxViewModel
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
                "AGENT_ADMIN" -> {
                    AgentHomeScreen(
                        agentVm = agentViewModel,
                        navController = navController
                    )
                }
                "AGENT_USER" ->{
                    ListingScreen(
                        listingVm = listingViewModel,
                        navController = navController
                    )
                }
                else -> {
                    HomeScreen(
                        navController = navController,
                        hm = homeViewModel
                    )
                }
            }

        }

        composable(NavigationItem.Inbox.route){
            InboxScreen(
                navController = navController,
                inboxVm = inboxViewModel
            )

        }
        composable(NavigationItem.OfferChat.route){
            OfferChatScreen(
                navController = navController,
                inboxVm = inboxViewModel
            )
        }

        composable(NavigationItem.MakeOffer.route){
            MakeOfferScreen(
                navController = navController,
                inboxVm = inboxViewModel
            )
        }


        composable(NavigationItem.AgentAgency.route){
            AgencySettingsScreen(
                agentVm = agentViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.AgentListings.route){
            ListingScreen(
                listingVm = listingViewModel,
                navController = navController
            )
        }


        composable(NavigationItem.SysAdminSupp.route){
            SysAdminSupportsScreen(
                sysAdminVm = systemAdminViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.FormUser.route) {
            SendEmailFormScreen(
                mailerSenderVm = mailerSenderViewModel,
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


        composable(NavigationItem.InboxScreen.route) {
            InboxScreen(
                navController = navController,
                inboxVm = inboxViewModel
            )
        }

        composable(NavigationItem.AppointmentChat.route) {
            AppointmentChatScreen(
                inboxVm = inboxViewModel,
                navController = navController
            )
        }

        composable(NavigationItem.BookAppointment.route) {
            BookAppointmentScreen(
                navController = navController,
                appointmentVM = appointmentViewModel,
                listingVm = listingViewModel,
                weatherVM = weatherViewModel
            )
        }

        composable(NavigationItem.CheckListingAppointment.route){
            CheckListingAppointmentScreen(
                navController = navController,
                appointmentVM = appointmentViewModel,
                listingVm = listingViewModel,
                weatherVM = weatherViewModel
            )
        }

        composable(NavigationItem.CheckAllAppointments.route){
            CheckAllAppointmentsScreen(
                navController = navController,
                appointmentVM = appointmentViewModel,
                weatherVM = weatherViewModel
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
                rm = resultsViewModel,
                listingVm = listingViewModel
            )
        }
    }
}

