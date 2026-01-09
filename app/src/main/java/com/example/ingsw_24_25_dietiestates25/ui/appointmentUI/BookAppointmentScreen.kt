package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Role
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.ListingViewModel
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.offerUI.InboxViewModel
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherViewModel
@Composable
fun BookAppointmentScreen(
    navController: NavController,
    appointmentVM: AppointmentViewModel,
    inboxVm: InboxViewModel,
    weatherVM: WeatherViewModel
) {
    val state by appointmentVM.state.collectAsState()
    val currentUser by appointmentVM.currentUser.collectAsState()

    val inboxState by inboxVm.state.collectAsState()
    val propertyListing = inboxState.selectedProperty ?: return

    val today = LocalDate.now()
    var selectedMonth by remember { mutableStateOf(YearMonth.from(today)) }

    LaunchedEffect(propertyListing.id) {
        appointmentVM.loadAppointmentsForListing(propertyListing.id, isForBooking = true)
    }

    val appointmentsThisMonth = state.appointments
        .filter { YearMonth.from(LocalDate.parse(it.date.toString())) == selectedMonth }
        .filter { it.listing.id == propertyListing.id }

    val appointmentsByDate = appointmentsThisMonth.groupBy { LocalDate.parse(it.date.toString()) }

    // Background elegante
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(WindowInsets.statusBars.asPaddingValues()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = bluPerchEcipiace,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { navController.popBackStack() }
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Book a Meeting",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = propertyListing.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        )
                    )
                }

                // RIGHT: Fake spacer to keep title perfectly centered
                Spacer(modifier = Modifier.size(32.dp))
            }




            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChevronLeft,
                        contentDescription = "Previous month",
                        tint = bluPerchEcipiace,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                selectedMonth = selectedMonth.minusMonths(1)
                            }
                    )

                    Text(
                        text = selectedMonth.month.getDisplayName(
                            TextStyle.FULL,
                            Locale.getDefault()
                        ).replaceFirstChar { it.uppercase() } + " ${selectedMonth.year}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Next month",
                        tint = bluPerchEcipiace,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                selectedMonth = selectedMonth.plusMonths(1)
                            }
                    )
                }
            }


            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    CalendarWithEvents(
                        month = selectedMonth,
                        occupiedDays = state.unavailableDates,
                        appointments = appointmentsByDate,
                        isForBooking = true,
                        onDaySelected = { day -> appointmentVM.selectDate(day) }
                    )
                }
            }
        }



        state.selectedDate?.let { day ->
            AppointmentBottomSheet(
                day = day,
                appointmentsForDay = appointmentsByDate[day] ?: emptyList(),
                propertyListing = propertyListing,
                weatherVM = weatherVM,
                appointmentVM = appointmentVM,
                isForBooking = true,
                onConfirm = {
                    propertyListing.let { appointmentVM.bookAppointment(it) }
                    appointmentVM.selectDate(null)
                    navController.popBackStack()
                },
                onDismiss = {
                    appointmentVM.selectDate(null)
                }
            )
        }


        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingOverlay(isVisible = true)
            }
        }


        state.resultMessage?.let { msg ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 14.dp)
            ) {
                Text(
                    text = msg,
                    color = if (state.success) Color(0xFF2E7D32) else Color.Red,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

