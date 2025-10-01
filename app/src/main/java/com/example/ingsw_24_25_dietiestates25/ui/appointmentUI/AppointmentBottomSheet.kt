package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate




import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentBottomSheet(
    day: LocalDate,
    appointmentsForDay: List<AppointmentUI>,
    propertyListing: PropertyListing?,
    weather: String? = null,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { Box(modifier = Modifier
            .height(4.dp)
            .width(40.dp)
            .background(Color.Gray)) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Intestazione giorno
            Text(
                "Riepilogo: ${day.dayOfMonth}/${day.monthValue}/${day.year}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(8.dp))

            // Riepilogo proprietà
            propertyListing?.let { listing ->
                Text("🏠 ${listing.title}", fontWeight = FontWeight.SemiBold)
                val prop = listing.property
                Text("📍 ${prop.city}, ${prop.street} ${prop.civicNumber}")
                Spacer(Modifier.height(8.dp))
            }

            // Meteo
            weather?.let {
                Text("☀️ Meteo: $it")
                Spacer(Modifier.height(8.dp))
            }

            // Lista appuntamenti del giorno
            if (appointmentsForDay.isEmpty()) {
                Text("Giorno libero, nessun appuntamento")
            } else {
                appointmentsForDay.forEach { app ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text("Ora: ${app.time}")
                        Text("Prenotato da: ${app.bookedBy}")
                    }
                    Spacer(Modifier.height(6.dp))
                }
            }

            Spacer(Modifier.weight(1f)) // spinge il bottone in basso

            Button(
                onClick = onConfirm,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Conferma")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_AppointmentBottomSheet() {
    val today = LocalDate.now()
    val fakeAppointments = listOf(
        AppointmentUI(today, "10:00", "casa","Mario Rossi", "listing1"),
        AppointmentUI(today, "15:30","casa", "Luca Bianchi", "listing1")
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
        type = Type.SELL,
        price = 250000f,
        property = fakeProperty,
        agent = null
    )

    AppointmentBottomSheet(
        day = today,
        appointmentsForDay = fakeAppointments,
        propertyListing = fakeListing,
        weather = "Soleggiato, 25°C",
        onDismiss = {},
        onConfirm = {}
    )
}
