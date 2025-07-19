package com.example.ingsw_24_25_dietiestates25.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ingsw_24_25_dietiestates25.R


// Colori personalizzati
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2C80B4), // blu pulsante login
    onPrimary = Color.White,
    secondary = Color(0xFF000000), // testo nero
    onSecondary = Color.White,
    background = Color(0xFFF8F9FA), // grigio molto chiaro
    surface = Color.White,
    onSurface = Color.Black,
)

val Gabarito = FontFamily(
    Font(R.font.gabarito_regular, FontWeight.Normal),
    Font(R.font.gabarito_medium, FontWeight.Medium),
    Font(R.font.gabarito_semibold, FontWeight.SemiBold),
    Font(R.font.gabarito_bold, FontWeight.Bold),
    Font(R.font.gabarito_extrabold, FontWeight.ExtraBold),
    Font(R.font.gabarito_black, FontWeight.Black)
)



@Composable
fun DietiEstatesTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(32.dp)
        ),
        content = content
    )
}