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
import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentBottomSheet(
    day: LocalDate,
    appointmentsForDay: List<Appointment>,
    propertyListing: PropertyListing?,
    weatherVM: WeatherViewModel,
    appointmentVM: AppointmentViewModel,
    isForBooking: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    propertyListing?.let { listing ->
        LaunchedEffect(listing.id, day) {
            weatherVM.loadWeatherInfo(listing, day)
        }
    }

    val daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), day)

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
        // ← rimosso fillMaxHeight() e Arrangement.SpaceBetween
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Titolo
            Column {
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    )
                )
                Text(
                    text = "${day.dayOfMonth}/${day.monthValue}/${day.year}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                )
            }

            // Immagine e info proprietà
            if (isForBooking && propertyListing != null) {
                if (propertyListing.property.images.isNotEmpty()) {
                    AsyncImage(
                        model = propertyListing.property.images.first(),
                        contentDescription = "Property photos",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = propertyListing.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 22.sp
                        )
                    )
                    Text(
                        text = "${propertyListing.property.city}, ${propertyListing.property.street} ${propertyListing.property.civicNumber}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                    )
                }
            }

            // Lista appuntamenti
            if (!isForBooking) {
                Text(
                    "Today's appointments:",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                )

                if (appointmentsForDay.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF3F3F3))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No appointments", color = Color.Gray, fontSize = 13.sp)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 160.dp)
                    ) {
                        items(appointmentsForDay) { app ->
                            AppointmentCard(appointment = app)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }

            // Meteo
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = BlueGray)
            ) {
                Box(modifier = Modifier.padding(10.dp)) {
                    when {
                        daysBetween > 8 -> {
                            Text(
                                text = "Weather not available",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = 12.sp
                            )
                        }
                        weatherVM.state.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        }
                        weatherVM.state.error != null -> {
                            Text(
                                weatherVM.state.error ?: "",
                                color = Color.Red,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 12.sp
                            )
                        }
                        else -> {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                WeatherCard(
                                    state = weatherVM.state,
                                    backgroundColor = BlueGray,
                                )
                            }
                        }
                    }
                }
            }

            // Pulsante Confirm — ora subito dopo il meteo
            if (isForBooking) {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AscientGradient)
                        .clickable { onConfirm() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Confirm",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
    }
}