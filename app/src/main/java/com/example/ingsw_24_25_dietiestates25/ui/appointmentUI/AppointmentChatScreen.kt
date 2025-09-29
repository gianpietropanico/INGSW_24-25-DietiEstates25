package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.compose.foundation.Image
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentMessage
import java.time.LocalDate

import com.example.ingsw_24_25_dietiestates25.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferMessage
import com.example.ingsw_24_25_dietiestates25.ui.offerUI.InboxViewModel
import com.example.ingsw_24_25_dietiestates25.ui.utils.safeDecodeBase64
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentChatScreen(
    inboxVm: InboxViewModel,
    navController: NavController
) {

    // Prendiamo lo stato dal ViewModel
    val state by inboxVm.state.collectAsState()
//    val currentUser = inboxVm.user.value?.id ?: ""
    val currentUser = inboxVm.user.value?.username ?: ""
    val selectedAppointment = state.selectedAppointment ?: return
    val appointments = selectedAppointment.appointmentMessages
    val firstImageBitmap =
        safeDecodeBase64(selectedAppointment.listing.property.images.firstOrNull())


    // LazyListState per scroll automatico
    val listState = rememberLazyListState()

    // Scroll automatico all’ultimo messaggio quando la lista cambia
    LaunchedEffect(appointments.size) {
        if (appointments.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Header: info venditore/prodotto
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    val agentPhoto = selectedAppointment.listing.agent?.profilePicture
                    if (agentPhoto != null) {
                        val bitmap = safeDecodeBase64(agentPhoto)
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap,
                                contentDescription = "Foto agente",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text = selectedAppointment.listing.agent?.name?.firstOrNull()?.uppercase() ?: "?",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Text(
                            text = selectedAppointment.listing.agent?.name?.firstOrNull()?.uppercase() ?: "?",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imageModifier = Modifier
                    .size(60.dp) // stessa misura del Box
                    .clip(RoundedCornerShape(8.dp)) // stessa forma
                    .background(Color.LightGray)    // sfondo fallback se serve

                if (firstImageBitmap != null) {
                    Image(
                        bitmap = firstImageBitmap,
                        contentDescription = selectedAppointment.listing.title,
                        contentScale = ContentScale.Crop,
                        modifier = imageModifier
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.default_house),
                        contentDescription = selectedAppointment.listing.title,
                        contentScale = ContentScale.Crop,
                        modifier = imageModifier
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = selectedAppointment.listing.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = listOfNotNull(
                            selectedAppointment.listing.property.city,
                            selectedAppointment.listing.property.street,
                            selectedAppointment.listing.property.civicNumber
                        ).joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


        }

        // Chat messages
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            reverseLayout = true
        ) {
            items(appointments.reversed()) { appointment ->
                AppointmentCardMessage(
                    date = appointment.date,
                    username = appointment.senderId,
                    currentUser = currentUser,
                    accepted = appointment.accepted,
                    onAccept = { inboxVm.acceptAppointment(selectedAppointment.id) },
                    onDecline = { inboxVm.rejectAppointment(selectedAppointment.id) },
                    onBookAgain = { inboxVm.bookNewAppointment(selectedAppointment.listing.id) }
                )
            }
        }
    }
}

@Composable
fun AppointmentCardMessage(
    date: LocalDate,
    username: String,
    currentUser: String,
    accepted: Boolean?, // stato richiesta
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onBookAgain: () -> Unit
) {
    val isMine = username == currentUser
    val bubbleColor = if (isMine) Color(0xFFD1F2EB) else Color.White
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.widthIn(max = 250.dp),
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (isMine) "La tua proposta di appuntamento" else "Richiesta ricevuta",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = date.format(formatter),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF006666)
                )

                if (!isMine) {
                    when (accepted) {
                        null -> { // ancora in sospeso
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = onAccept,
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFF2E7D32
                                        )
                                    )
                                ) { Text("Accetta") }
                                OutlinedButton(
                                    onClick = onDecline,
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(
                                            0xFFC62828
                                        )
                                    )
                                ) { Text("Rifiuta") }
                            }
                        }

                        false -> { // rifiutata
                            Button(
                                onClick = onBookAgain,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF2E7D32
                                    )
                                )
                            ) { Text("Prenota nuovo appuntamento", color = Color.White) }
                        }

                        true -> { /* accettata → nessun bottone */
                        }
                    }
                }
            }
        }
    }
}

