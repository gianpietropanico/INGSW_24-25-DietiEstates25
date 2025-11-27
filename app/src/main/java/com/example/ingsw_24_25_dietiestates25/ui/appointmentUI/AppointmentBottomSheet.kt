package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient
import com.example.ingsw_24_25_dietiestates25.ui.theme.BlueGray
import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherCard
import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherForecast

import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherViewModel
import java.time.LocalDate
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentBottomSheet(
    day: LocalDate,
    appointmentsForDay: List<Appointment>,
    propertyListing: PropertyListing?,
    weatherVM: WeatherViewModel,
    appointmentVM: AppointmentViewModel,
    isForBooking: Boolean = false,
    onDismiss: () -> Unit
) {

    propertyListing?.let { listing ->
        LaunchedEffect(listing.id, day) {
            weatherVM.loadWeatherInfo(listing, day)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
        containerColor = Color.White,
        tonalElevation = 6.dp,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 6.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0x33000000))
                )
            }
        }
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {

                // ---------- Title ----------
                Column {
                    Text(
                        text = "Riepilogo",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "${day.dayOfMonth}/${day.monthValue}/${day.year}",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                    )
                }

                // ---------- IMMAGINE E INFO PROPRIETÀ ----------
                if (isForBooking && propertyListing != null) {

                    if (propertyListing.property.images.isNotEmpty()) {
                        AsyncImage(
                            model = propertyListing.property.images.first(),
                            contentDescription = "Foto proprietà",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        Text(
                            text = propertyListing.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "${propertyListing.property.city}, ${propertyListing.property.street} ${propertyListing.property.civicNumber}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                    }
                }

                // ---------- LISTA APPUNTAMENTI (solo modalità agente) ----------
                if (!isForBooking) {
                    Text(
                        "Appuntamenti del giorno:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    if (appointmentsForDay.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF3F3F3))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nessun appuntamento",
                                color = Color.Gray
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 180.dp)
                        ) {
                            items(appointmentsForDay) { app ->
                                AppointmentCard(appointment = app)
                                Spacer(Modifier.height(10.dp))
                            }
                        }
                    }
                }

                // ---------- METEO ----------
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(3.dp),
                    colors = CardDefaults.cardColors(containerColor = BlueGray)
                ) {

                    Box(modifier = Modifier.padding(14.dp)) {

                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            WeatherCard(
                                state = weatherVM.state,
                                backgroundColor = BlueGray
                            )
                            WeatherForecast(state = weatherVM.state)
                        }

                        if (weatherVM.state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.White
                            )
                        }

                        weatherVM.state.error?.let { err ->
                            Text(
                                err,
                                color = Color.Red,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }

            // ---------- PULSANTE CONFERMA ----------
            if (isForBooking) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AscientGradient)
                        .clickable {
                            propertyListing?.let { appointmentVM.bookAppointment(it) }
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Conferma",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
