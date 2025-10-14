package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment

@Composable
fun AppointmentCard(
    appointment: Appointment,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barra colorata a sinistra
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(48.dp)
                    .background(Color(0xFF1976D2))
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Date: ${appointment.date}", fontWeight = FontWeight.SemiBold)
                Text("Proprerty: ${appointment.listing.title}")
                Text("Address ${appointment.listing.property.city}, ${appointment.listing.property.street}, ${appointment.listing.property.civicNumber}")
                Text("Booking by: ${appointment.user.name}")
            }
        }
    }
}