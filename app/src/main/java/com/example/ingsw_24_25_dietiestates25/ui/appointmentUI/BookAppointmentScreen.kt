package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

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
import androidx.compose.foundation.layout.statusBars
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
    listingVm: ListingViewModel,
    weatherVM: WeatherViewModel
) {
    val state by appointmentVM.state.collectAsState()
    val currentUser by appointmentVM.currentUser.collectAsState()

    val listingState by listingVm.state.collectAsState()
    val propertyListing = listingState.selectedListing ?: return

    val today = LocalDate.now()
    var selectedMonth by remember { mutableStateOf(YearMonth.from(today)) }

    LaunchedEffect(propertyListing.id) {
        appointmentVM.loadAppointmentsForListing(propertyListing.id, isForBooking = true)
    }

// Filtra appuntamenti solo per il mese selezionato E per il listing selezionato
    val appointmentsThisMonth = state.appointments
        .filter { YearMonth.from(LocalDate.parse(it.date.toString())) == selectedMonth }
        .filter { it.listing.id == propertyListing.id }

// Raggruppa per giorno
    val appointmentsByDate = appointmentsThisMonth.groupBy { LocalDate.parse(it.date.toString()) }


    Column(modifier = Modifier.padding(8.dp)
        .padding(WindowInsets.statusBars.asPaddingValues())) {

        // Header mese
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "<",
                fontSize = 24.sp,
                modifier = Modifier.clickable { selectedMonth = selectedMonth.minusMonths(1) }
            )
            Text(
                "${
                    selectedMonth.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    )
                } ${selectedMonth.year}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                ">",
                fontSize = 24.sp,
                modifier = Modifier.clickable { selectedMonth = selectedMonth.plusMonths(1) }
            )
        }

        Spacer(Modifier.height(12.dp))


        // Calendario
        CalendarWithEvents(
            month = selectedMonth,
            occupiedDays = state.unavailableDates,
            appointments = appointmentsByDate,
            isForBooking = true,
            onDaySelected = { day -> appointmentVM.selectDate(day) }
        )
    }


    // BottomSheet solo se giorno selezionato
    state.selectedDate?.let { day ->
        val appointmentsForDay = appointmentsByDate[day] ?: emptyList()
        AppointmentBottomSheet(
            day = day,
            appointmentsForDay = appointmentsForDay,
            propertyListing = propertyListing,
            weatherVM,
            appointmentVM = appointmentVM,
            isForBooking = true,
            onDismiss = { appointmentVM.selectDate(null) }
        )
    }

    // Loading e messaggi
    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingOverlay(isVisible = true)
        }
    }

    state.resultMessage?.let { msg ->
        Text(
            msg,
            color = if (state.success) Color.Green else Color.Red,
            modifier = Modifier.padding(8.dp)
        )
    }


}
