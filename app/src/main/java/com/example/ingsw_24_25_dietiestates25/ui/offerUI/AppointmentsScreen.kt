package com.example.ingsw_24_25_dietiestates25.ui.offerUI
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.ListingSummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertySummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Role
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.ui.appointmentUI.AppointmentBottomSheet
import com.example.ingsw_24_25_dietiestates25.ui.appointmentUI.AppointmentViewModel
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.utils.weather.WeatherViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun UserAppointmentsScreen(
    inboxVm : InboxViewModel,
    navController: NavController,
    appointmentVM: AppointmentViewModel,
    weatherVM: WeatherViewModel
) {
    val state by appointmentVM.state.collectAsState()
    val inboxState by inboxVm.state.collectAsState()
    val currentUser by inboxVm.user.collectAsState()

    val today = LocalDate.now()
    var selectedMonth by remember { mutableStateOf(YearMonth.from(today)) }

    val userAppointments: List<Appointment> = emptyList()

    // Carico appuntamenti dell’utente
    LaunchedEffect(currentUser?.id) {
        currentUser?.id?.let { inboxVm.loadAppointmentsForUser(currentUser!!.id) }
    }

    // Appuntamenti filtrati per mese
    val appointmentsThisMonth = inboxState.userAppointments.filter { YearMonth.from(it.date) == selectedMonth }

    val appointmentsByDate = appointmentsThisMonth.groupBy { it.date }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(WindowInsets.statusBars.asPaddingValues()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = bluPerchEcipiace,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { navController.popBackStack() }
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Your Appointments",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.size(32.dp))
            }

            // Mese
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChevronLeft,
                        contentDescription = "Previous month",
                        tint = bluPerchEcipiace,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { selectedMonth = selectedMonth.minusMonths(1) }
                    )

                    Text(
                        text = selectedMonth.month.getDisplayName(
                            TextStyle.FULL,
                            Locale.getDefault()
                        ).replaceFirstChar { it.uppercase() } + " ${selectedMonth.year}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Next month",
                        tint = bluPerchEcipiace,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { selectedMonth = selectedMonth.plusMonths(1) }
                    )
                }
            }

            // Calendario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    UserAppointmentCalendar(
                        month = selectedMonth,
                        userAppointments = appointmentsByDate,
                        onDaySelected = { day -> appointmentVM.selectDate(day) }
                    )
                }
            }
        }

        // Bottom sheet
//        state.selectedDate?.let { day ->
//            AppointmentBottomSheet(
//                day = day,
//                appointmentsForDay = appointmentsByDate[day] ?: emptyList(),
//                propertyListing = null,
//                weatherVM = weatherVM,
//                appointmentVM = appointmentVM,
//                isForBooking = false,
//                onDismiss = { appointmentVM.selectDate(null) }
//            )
//        }
    }
}


@Composable
fun UserAppointmentCalendar(
    month: YearMonth,
    userAppointments: Map<LocalDate, List<Appointment>>,
    onDaySelected: (LocalDate) -> Unit
) {
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }
    val today = LocalDate.now()

    val eventDays = userAppointments.keys

    val firstOfMonth = month.atDay(1)
    val offset = (firstOfMonth.dayOfWeek.value % 7)
    val start = firstOfMonth.minusDays(offset.toLong())

    Column {

        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        for (row in 0 until 6) {
            Row(modifier = Modifier.fillMaxWidth()) {

                for (col in 0 until 7) {
                    val day = start.plusDays((row * 7 + col).toLong())
                    val isInMonth = day.month == month.month
                    val hasEvents = day in eventDays

                    val bgColor = when {
                        selectedDay == day -> Color(0x332196F3)
                        hasEvents -> Color(0x332196F3)
                        else -> Color.Transparent
                    }

                    val isClickable = hasEvents && isInMonth

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(bgColor)
                            .clickable(enabled = isClickable) {
                                selectedDay = day
                                onDaySelected(day)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.dayOfMonth.toString(),
                            color = when {
                                hasEvents -> Color(0xFF0D47A1)
                                !isInMonth -> Color.LightGray
                                else -> Color.Black
                            },
                            fontWeight = if (hasEvents || day == today) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewUserAppointmentsScreen() {

    // ---- FAKE DATA ----

    val fakeUser = User(
        id = "u1",
        username = "giuseppe",
        email = "giuseppe@example.com",
        role = Role.LOCAL_USER
    )

    val fakeProperty = PropertySummary(
        city = "Torino",
        street = "Via Roma",
        civicNumber = "12",
        images = emptyList()
    )

    val fakeListing = ListingSummary(
        id = "l1",
        title = "Appartamento luminoso",
        property = fakeProperty
    )

    val fakeAppointments = listOf(
        Appointment(
            id = "a1",
            listing = fakeListing,
            user = fakeUser,
            agent = fakeUser,
            date = LocalDate.now().plusDays(1)
        ),
        Appointment(
            id = "a2",
            listing = fakeListing,
            user = fakeUser,
            agent = fakeUser,
            date = LocalDate.now().plusDays(4)
        ),
        Appointment(
            id = "a3",
            listing = fakeListing,
            user = fakeUser,
            agent = fakeUser,
            date = LocalDate.now().plusDays(10)
        )
    )

    // ---- FAKE VIEWMODELS ----


//
//    UserAppointmentsScreen(
//        inboxVm = fakeInboxVM,
//        navController = fakeNavController,
//        appointmentVM = fakeAppointmentVM,
//        weatherVM = fakeWeatherVM
//    )
}
