package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ingsw_24_25_dietiestates25.ui.appointmentUI.AppointmentUI
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun CheckListingAppointmentScreen(
    appointments: List<AppointmentUI>
) {
    val today = LocalDate.now()
    var selectedMonth by remember { mutableStateOf(YearMonth.from(today)) }

    // Filtra appuntamenti solo per il mese selezionato
    val appointmentsThisMonth = appointments.filter {
        YearMonth.from(it.date) == selectedMonth
    }

    // Raggruppa per giorno
    val appointmentsByDate = appointmentsThisMonth.groupBy { it.date }

    Column(modifier = Modifier.padding(8.dp)) {
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

        // Calendario
        CalendarWithEvents(
            month = selectedMonth,
            occupiedDays = appointmentsByDate.keys,
            appointments = appointmentsByDate,
            onDaySelected = {}
        )

        Spacer(Modifier.height(12.dp))

        // Lista appuntamenti
        LazyColumn {
            appointmentsByDate.toSortedMap().forEach { (date, list) ->
                item {
                    Text(
                        text = "📅 ${date.dayOfMonth}/${date.monthValue}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(list) { app ->
                    AppointmentCard(appointment = app)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview_CheckListingAppointmentScreen() {
    val today = LocalDate.now()
    val fakeAppointments = listOf(
        AppointmentUI(today, "10:00", "casa", "Mario Rossi", "listing1"),
        AppointmentUI(today, "15:30", "casa", "Luca Bianchi", "listing1")
    )

    CheckListingAppointmentScreen(appointments = fakeAppointments)
}