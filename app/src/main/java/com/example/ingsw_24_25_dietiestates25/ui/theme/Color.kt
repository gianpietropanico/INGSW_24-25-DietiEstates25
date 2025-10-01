package com.example.ingsw_24_25_dietiestates25.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush


val AscientGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF5B99C0), // start color
        Color(0xFF1688CF)  // end color
    )
)
val Black = Color(0xFF000113)
val LightBlueWhite = Color(0xFFF1F5F9) //Social media background
val BlueGray = Color(0xFF334155)
val primaryBlu = Color(0xFF3A7CA5)
val testColor = Color(0xFF2F6690)
val primaryBlueWithOpacity: Color = primaryBlu.copy(alpha = 0.1f)
val DarkRed = Color(0xFFC62828)
val bluPerchEcipiace = Color(0xFF2F6690)
val unselectedFacility = Color(0xFFF5F4F8)

val pageBackgroundColor = Color(0xFF0E0E0E)
val itemBackgroundColor = Color(0xFF1B1B1B)
val toolbarColor = Color(0xFF282828)
val selectedItemColor = Color(0xFFDCDCDC)
val inActiveTextColor = Color(0xFF616161)



val cardBackground = Color(0xFFD9D9D9)


val ColorScheme.myFocusedTextFieldText: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black

val ColorScheme.unFocusedTextFieldText: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color( 0xff94A3B8) else Color(0XFF475569)

val ColorScheme.textFieldContainer: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black

