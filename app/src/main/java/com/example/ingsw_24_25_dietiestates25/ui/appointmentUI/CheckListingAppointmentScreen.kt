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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentStatus
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Role
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.ListingViewModel
import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherViewModel


@Composable
fun CheckListingAppointmentScreen(
    navController: NavController,
    appointmentVM: AppointmentViewModel,
    listingVm: ListingViewModel,
    weatherVM: WeatherViewModel
) {
    val state by appointmentVM.state.collectAsState()
    val currentUser by appointmentVM.currentUser.collectAsState()


    val listingState by listingVm.state.collectAsState()
    val propertyListing = listingState.selectedListing ?: return

    val today = LocalDate.now()
    var selectedMonth by remember { mutableStateOf(YearMonth.from(today)) }

    appointmentVM.loadAppointmentsForListing(propertyListing.id, isForBooking = false)
// Filtra appuntamenti solo per il mese selezionato E per il listing selezionato
    val appointmentsThisMonth = state.appointments
        .filter { YearMonth.from(LocalDate.parse(it.date.toString())) == selectedMonth }
        .filter { it.listing.id == propertyListing.id }
        .filter { it.status != AppointmentStatus.REJECTED }

// Raggruppa per giorno
    val appointmentsByDate = appointmentsThisMonth.groupBy { LocalDate.parse(it.date.toString()) }

    Column(modifier = Modifier.padding(8.dp)
        .padding(WindowInsets.statusBars.asPaddingValues())) {
        // Intestazione mese con navigazione
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "<",
                fontSize = 24.sp,
                modifier = Modifier
                    .clickable { selectedMonth = selectedMonth.minusMonths(1) }
                    .padding(8.dp)
            )
            Text(
                text = "${
                    selectedMonth.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    )
                } ${selectedMonth.year}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = ">",
                fontSize = 24.sp,
                modifier = Modifier
                    .clickable { selectedMonth = selectedMonth.plusMonths(1) }
                    .padding(8.dp)
            )
        }

        Spacer(Modifier.height(12.dp))


        // Calendario con giorni occupati
        CalendarWithEvents(
            month = selectedMonth,
            occupiedDays = state.unavailableDates,
            appointments = appointmentsByDate,
            isForBooking = false,
            onDaySelected = { date ->
                appointmentVM.selectDate(date)
            }
        )

        Spacer(Modifier.height(12.dp))


        // Lista appuntamenti per il mese
        LazyColumn {
            appointmentsByDate.toSortedMap().forEach { (date, list) ->
                item {
                    Text(
                        text = "${date.dayOfMonth}/${date.monthValue}/${date.year}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(list) { appointment ->
                    AppointmentCard(appointment = appointment)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        state.selectedDate?.let { day ->
            val appointmentsForDay = appointmentsByDate[day] ?: emptyList()
            AppointmentBottomSheet(
                day = day,
                appointmentsForDay = appointmentsForDay,
                propertyListing = propertyListing,
                weatherVM,
                appointmentVM = appointmentVM,
                isForBooking = false,
                onConfirm = {
                    propertyListing?.let { appointmentVM.bookAppointment(it) }
                    appointmentVM.selectDate(null)
                    navController.popBackStack() // torna a OfferChatScreen
                },
                onDismiss = {
                    appointmentVM.selectDate(null)
                }
            )
        }
    }
}



