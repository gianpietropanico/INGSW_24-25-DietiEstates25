package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.compose.foundation.background
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
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Riepilogo: ${day.dayOfMonth}/${day.monthValue}/${day.year}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(8.dp))

                if (isForBooking) {
                    propertyListing?.let { listing ->

                        if (listing.property.images.isNotEmpty()) {
                            AsyncImage(
                                model = listing.property.images.first(),
                                contentDescription = "Foto proprietà",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Text(" ${listing.title}", fontWeight = FontWeight.SemiBold)
                        val prop = listing.property
                        Text(" ${prop.city}, ${prop.street} ${prop.civicNumber}")
                        Spacer(Modifier.height(8.dp))
                    }
                }

                if (!isForBooking) {
                    Text("Appuntamenti del giorno:", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    if (appointmentsForDay.isEmpty()) {
                        Text("Nessun appuntamento")
                    } else {
                        LazyColumn {
                            items(appointmentsForDay) { app ->
                                AppointmentCard(appointment = app)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }

                // Meteo
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BlueGray)
                    ) {
                        WeatherCard(
                            state = weatherVM.state,
                            backgroundColor = BlueGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        WeatherForecast(state = weatherVM.state)
                    }

                    if (weatherVM.state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    weatherVM.state.error?.let { error ->
                        Text(
                            text = error,
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            if (isForBooking) {
                Button(
                    onClick = {
                        propertyListing?.let { appointmentVM.bookAppointment(it) }
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Conferma")
                }
            }
        }
    }
}