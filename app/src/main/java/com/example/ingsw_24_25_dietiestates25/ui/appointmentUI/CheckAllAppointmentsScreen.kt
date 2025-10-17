package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentStatus
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Role
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.ListingViewModel
import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CheckAllAppointmentsScreen(
    navController: NavController,
    appointmentVM: AppointmentViewModel,
    weatherVM: WeatherViewModel
) {
    val state by appointmentVM.state.collectAsState()
    val currentUser by appointmentVM.currentUser.collectAsState()


    val today = LocalDate.now()
    var selectedMonth by remember { mutableStateOf(YearMonth.from(today)) }

    // Tutti gli appuntamenti del mese selezionato (nessun filtro per listing)
    val appointmentsThisMonth = state.appointments
        .filter { YearMonth.from(LocalDate.parse(it.date.toString())) == selectedMonth }
        .filter { it.status != AppointmentStatus.REJECTED }

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
            occupiedDays = appointmentsByDate.keys,
            appointments = appointmentsByDate,
            isForBooking = false,
            onDaySelected = { day -> appointmentVM.selectDate(day) }
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
            CheckAllAppointmentsBottomSheet(
                day = day,
                appointmentsForDay = appointmentsForDay,
                appointmentVM = appointmentVM,
                onDismiss = { appointmentVM.selectDate(null) }
            )
        }
    }
}