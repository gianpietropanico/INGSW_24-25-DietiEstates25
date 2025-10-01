package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun BookAppointmentScreen(
    appointments: List<AppointmentUI>,
    propertyListing: PropertyListing

) {


    val today = LocalDate.now()
    var selectedMonth by remember { mutableStateOf(YearMonth.from(today)) }
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }

    val appointmentsThisMonth = appointments.filter { YearMonth.from(it.date) == selectedMonth }
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

        // Calendario con dot per giorni occupati
        CalendarWithEvents(
            month = selectedMonth,
            occupiedDays = appointmentsByDate.keys,
            appointments = appointmentsByDate,
            onDaySelected = { day -> selectedDay = day }
        )

        Spacer(Modifier.height(12.dp))
    }

    selectedDay?.let { day ->
        AppointmentBottomSheet(
            day = day,
            appointmentsForDay = appointmentsByDate[day] ?: emptyList(),
            onDismiss = { selectedDay = null },
            onConfirm = {},
            propertyListing = propertyListing

        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_BookAppointmentScreen() {
    val today = LocalDate.now()

    val fakeAppointments = listOf(
        AppointmentUI(
            date = today.plusDays(2),
            time = "10:00",
            propertyTitle = "Appartamento Via Roma 12",
            bookedBy = "Mario Rossi",
            listingId = "34577"
        ),
        AppointmentUI(
            date = today.plusDays(5),
            time = "11:30",
            propertyTitle = "Villa Corso Milano",
            bookedBy = "Luca Bianchi",
            listingId = "34577"


        )
    )
    val fakeProperty = com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property(
        city = "Milano",
        cap = "20100",
        country = "Italia",
        province = "MI",
        street = "Via Roma",
        civicNumber = "12",
        latitude = 45.4642,
        longitude = 9.1900,
        numberOfRooms = 3,
        numberOfBathrooms = 2,
        size = 80f,
        energyClass = com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass.B,
        parking = true,
        garden = false,
        elevator = true,
        gatehouse = false,
        balcony = true,
        roof = false,
        airConditioning = true,
        heatingSystem = true,
        description = "Bellissimo appartamento nel centro"
    )

    val fakeListing = PropertyListing(
        id = "listing1",
        title = "Appartamento Via Roma 12",
        type = com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type.SELL,
        price = 250000f,
        property = fakeProperty,
        agent = null
    )

    BookAppointmentScreen(appointments = fakeAppointments, propertyListing = fakeListing)
}
