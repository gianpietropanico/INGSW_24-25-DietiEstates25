package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.ui.theme.BlueGray
import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherCard
import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherForecast
import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherViewModel
import java.time.LocalDate
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckAllAppointmentsBottomSheet(
    day: LocalDate,
    appointmentsForDay: List<Appointment>,
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
            Text(
                "Summary: ${day.dayOfMonth}/${day.monthValue}/${day.year}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(8.dp))




            Text("Today's appointments:", fontWeight = FontWeight.Bold)
            if (appointmentsForDay.isEmpty()) {
                Text("No appointment")
            } else {
                LazyColumn {
                    items(appointmentsForDay) { app ->
                        AppointmentCard(appointment = app)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))


        }
    }


}
