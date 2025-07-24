package com.example.ingsw_24_25_dietiestates25.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF000113)
val LightBlueWhite = Color(0xFFF1F5F9) //Social media background
val BlueGray = Color(0xFF334155)
val primaryBlu = Color(0xFF3A7CA5)
val primaryBlueWithOpacity: Color = primaryBlu.copy(alpha = 0.1f)
val DarkRed = Color(0xFFC62828)

val ColorScheme.myFocusedTextFieldText: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black

val ColorScheme.unFocusedTextFieldText: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color( 0xff94A3B8) else Color(0XFF475569)

val ColorScheme.textFieldContainer: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black