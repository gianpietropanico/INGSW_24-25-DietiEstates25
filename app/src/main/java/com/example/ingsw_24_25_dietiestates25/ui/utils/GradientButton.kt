package com.example.ingsw_24_25_dietiestates25.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = Modifier
            .width(320.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF5B99C0),
                        Color(0xFF1688CF)
                    ),
                    start = Offset(0f,0f),
                    end = Offset(1000f, 0f)
                )
            )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.rubik_semibold)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                letterSpacing = 0.sp
            ),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}


@Composable
fun GradientButtonWhite(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        contentPadding = PaddingValues(),
        modifier = Modifier
            .width(320.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(50.dp))
            .border(2.dp, AscientGradient, RoundedCornerShape(50.dp))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = FontFamily(Font(R.font.rubik_semibold)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                letterSpacing = 0.sp,
                color = Color(0xFF1688CF)
            ),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
