package com.example.ingsw_24_25_dietiestates25.ui.utils.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.WeatherInfo

@Composable
fun WeatherRow(weatherForecast: List<WeatherInfo>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(weatherForecast) { w ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(70.dp)
                    .padding(4.dp)
            ) {
                Text(w.time, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)

                // se usi emoji
                Text(
                    text = when (w.condition.lowercase()) {
                        "soleggiato" -> "☀️"
                        "parzialmente nuvoloso" -> "⛅"
                        "nuvoloso" -> "☁️"
                        "pioggia" -> "🌧️"
                        else -> "❓"
                    },
                    fontSize = 20.sp
                )

                // se usi icone reali
                // AsyncImage(model = w.iconUrl, contentDescription = "Weather", modifier = Modifier.size(32.dp))

                Text(w.temperature, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}