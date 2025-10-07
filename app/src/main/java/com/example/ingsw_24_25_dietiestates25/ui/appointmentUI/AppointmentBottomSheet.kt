package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import WeatherChart
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
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.WeatherInfo
import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherRow
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentBottomSheet(
    day: LocalDate,
    appointmentsForDay: List<AppointmentUI>,
    propertyListing: PropertyListing?,
    weatherForecast: List<WeatherInfo>? = null,
    appointmentVM: AppointmentViewModel,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = {
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(40.dp)
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
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
            weatherForecast?.let { forecasts ->
                if (forecasts.isNotEmpty()) {
                    Text("🌡️ Andamento temperature:", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    WeatherRow(forecasts)
                    Spacer(Modifier.height(8.dp))
                    WeatherChart(forecasts)
                }
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

            Spacer(Modifier.weight(1f))

            // Pulsante Conferma
            Button(
                onClick = {
                    propertyListing?.let { appointmentVM.bookAppointment(it) }
                    onDismiss()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Conferma")
            }
        }
    }
}