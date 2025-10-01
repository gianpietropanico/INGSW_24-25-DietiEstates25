package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import java.time.format.TextStyle

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale


@Composable
fun CalendarWithEvents(
    month: YearMonth,
    occupiedDays: Set<LocalDate>,
    appointments: Map<LocalDate, List<AppointmentUI>> = emptyMap(),
    onDaySelected: (LocalDate) -> Unit
) {
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }
    val today = LocalDate.now()
    val firstOfMonth = month.atDay(1)
    val offset = (firstOfMonth.dayOfWeek.value % 7)
    val start = firstOfMonth.minusDays(offset.toLong())

    Column {
        // Giorni settimana
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun").forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Griglia giorni
        for (row in 0 until 6) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val day = start.plusDays((row * 7 + col).toLong())
                    val isInMonth = day.month == month.month
                    val isOccupied = day in occupiedDays
                    val isPast = day.isBefore(today)

                    val bgColor = when {
                        selectedDay == day -> Color(0x3301976D2) // trasparenza per selezionato
                        isOccupied -> Color(0xFFFFCDD2)
                        else -> Color.Transparent
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(bgColor)
                            .clickable(enabled = isInMonth && !isPast) {
                                selectedDay = day
                                onDaySelected(day)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = day.dayOfMonth.toString(),
                                color = if (isInMonth) Color.Black else Color.LightGray,
                                fontWeight = if (day == today) FontWeight.Bold else FontWeight.Normal
                            )

//                            if ((appointments[day]?.size ?: 0) > 0) {
//                                Box(
//                                    modifier = Modifier
//                                        .size(5.dp)
//                                        .clip(CircleShape)
//                                        .background(Color.Blue)
//                                )
//                            }
                        }
                    }
                }
            }
        }
    }
}
