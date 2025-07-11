package com.example.ingsw_24_25_dietiestates25.ui.theme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.R


val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = Gabarito,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,              // 32px ≈ 24sp
        lineHeight = 22.sp,
        letterSpacing = 0.32.sp        // 1% of 32px = ~0.32px ≈ 0.32sp
    ),
    bodyLarge = TextStyle(
        fontFamily = RobotoSlab,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Gabarito,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Gabarito,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    labelMedium = TextStyle(
        fontFamily = RobotoSlab,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)
