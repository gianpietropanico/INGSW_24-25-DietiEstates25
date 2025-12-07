package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentStatus

@Composable
fun AppointmentCard(
    appointment: Appointment,
    modifier: Modifier = Modifier
) {
    // Colore della barra in base allo stato
    val statusColor = when (appointment.status) {
        AppointmentStatus.ACCEPTED -> Color(0xFF2E7D32) // verde
        AppointmentStatus.REJECTED -> Color(0xFFC62828) // rosso
        AppointmentStatus.PENDING -> Color(0xFF1976D2)  // blu
    }

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
                    .height(80.dp)
                    .background(statusColor)
            )

            Spacer(Modifier.width(12.dp))

            if (appointment.listing.property.images.isNotEmpty()) {
                AsyncImage(
                    model = appointment.listing.property.images.first(),
                    contentDescription = "Immagine della proprietà",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.default_house),
                    contentDescription = "Immagine di default",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(12.dp))

            // Dettagli appuntamento
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${appointment.date}",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    appointment.listing.title,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "${appointment.listing.property.city}, ${appointment.listing.property.street} ${appointment.listing.property.civicNumber}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "Booking by: ${appointment.user.username}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    "Status: ${appointment.status}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}