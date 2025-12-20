package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.*
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.offerUI.InboxViewModel
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentChatScreen(
    inboxVm: InboxViewModel,
    navController: NavController
) {
    val state by inboxVm.state.collectAsState()
    val currentUser = inboxVm.getCurrentUser()
    val appointment = state.selectedAppointment

    if (appointment == null || currentUser == null) {
        LoadingOverlay(true)
        return
    }

    val isAgent = currentUser.id == appointment.agent.id
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Appointment Chat",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF006666),
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.popBackStack() }
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            //Property section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(73.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF006666)),
                    contentAlignment = Alignment.Center
                ) {
                    if (appointment.listing.property.images.isNotEmpty()) {
                        AsyncImage(
                            model = appointment.listing.property.images.first(),
                            contentDescription = "Property Image",
                            modifier = Modifier.fillMaxSize().clip(RectangleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.default_house),
                            error = painterResource(id = R.drawable.default_house)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.default_house),
                            contentDescription = "Default House",
                            modifier = Modifier.fillMaxSize().clip(RectangleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = appointment.listing.title,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = appointment.listing.property.street,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "Date: ${appointment.date.format(formatter)}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }
            }

            //Status section
            AppointmentStatusBox(appointment.status)

            Spacer(Modifier.height(8.dp))

            //Messages
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                items(appointment.appointmentMessages) { message ->
                    AppointmentMessageCard(
                        message = message,
                        currentUser = currentUser
                    )
                }
            }

            //Action buttons
            if (isAgent && appointment.status == AppointmentStatus.PENDING) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { inboxVm.acceptAppointment(appointment.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Accept", color = Color.White)
                    }

                    OutlinedButton(
                        onClick = { inboxVm.declineAppointment(appointment.id) },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, DarkRed)
                    ) {
                        Text("Decline", color = DarkRed)
                    }
                }
            }
        }
    }
}

@Composable
fun AppointmentStatusBox(status: AppointmentStatus) {
    val (bgColor, textColor, text) = when (status) {
        AppointmentStatus.ACCEPTED -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Appointment accepted")
        AppointmentStatus.REJECTED -> Triple(Color(0xFFFFEBEE), DarkRed, "Appointment declined")
        AppointmentStatus.PENDING -> Triple(Color(0xFFFFF8E1), Color(0xFFFFA000), "Awaiting confirmation")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun AppointmentMessageCard(
    message: AppointmentMessage,
    currentUser: User
) {
    val isMine = message.sender.username == currentUser.username
    val bubbleColor = if (isMine) Color(0xFFDFF6FF) else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Status: ${message.status?.name ?: "No status"}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Date: ${message.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "By: ${message.sender.username}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
                )
            }
        }
    }
}
