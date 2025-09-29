package com.example.ingsw_24_25_dietiestates25.ui.offerUI

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferMessage
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.ProfileViewModel
import com.example.ingsw_24_25_dietiestates25.ui.utils.DietiNavBar
import com.example.ingsw_24_25_dietiestates25.ui.utils.Screen
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue


@Composable
fun InboxScreen(
    navController : NavController,
    inboxVm : InboxViewModel
) {

    val user by inboxVm.user.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val state by inboxVm.state.collectAsState()

    // Stato per la tab selezionata
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        inboxVm.loadOffers()
        inboxVm.loadAppointments()
    }

    Scaffold(
        bottomBar = {
            DietiNavBar(
                currentRoute = currentRoute ?: Screen.Home.route,
                onRouteSelected = { newRoute ->
                    navController.navigate(newRoute) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }, text = { Text("Messages ${state.offers.size}") })
                    Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }, text = { Text("Appointment ${state.appointments.size}") })
                }

                LazyColumn {
                    if (selectedTabIndex == 0) {
                        items(state.offers) { offer ->
                            OfferItem(offer, user!!.username, inboxVm, navController)
                            HorizontalDivider()
                        }
                    } else {
                        items(state.appointments) { appointment ->
                            AppointmentItem(
                                appointment = appointment,
                                currentUserId = user!!.id,
                                inboxVm = inboxVm,
                                onClick = {
                                    inboxVm.setSelectedAppointment(appointment)
                                    navController.navigate("appointmentChat")
                                }
                            )
                            HorizontalDivider()
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun OfferItem(
    offer: Offer,
    currentUserId: String,
    inboxVm: InboxViewModel,
    navController: NavController
) {
    val lastMessage = offer.messages.lastOrNull()
    val otherUser = if (offer.agentName == currentUserId) offer.agentName else offer.buyerName

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                inboxVm.setSelectedOffer(offer)
                navController.navigate(NavigationItem.OfferChat.route)
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF006666)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = otherUser.firstOrNull()?.uppercase() ?: "?",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = otherUser,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Immobile: ${offer.propertyId}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = lastMessage?.timestamp?.toDaysAgo() ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
fun AppointmentItem(
    appointment: Appointment,
    currentUserId: String,
    inboxVm : InboxViewModel,
    onClick: (Appointment) -> Unit // callback
) {
    val lastMessage = appointment.appointmentMessages.lastOrNull()
    val otherUser = if (appointment.agent.id == currentUserId) appointment.agent else appointment.user

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(appointment) } // chiama la callback
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF006666)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = otherUser.username.firstOrNull()?.uppercase() ?: "?",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = otherUser.username,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Immobile: ${appointment.listing.id}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = lastMessage?.timestamp?.toDaysAgo() ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

fun Long.toDaysAgo(): String {
    val days = (System.currentTimeMillis() - this) / (1000 * 60 * 60 * 24)
    return if (days == 0L) "Oggi" else "$days giorni fa"
}