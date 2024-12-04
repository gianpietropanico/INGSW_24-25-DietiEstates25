package com.example.ingsw_24_25_dietiestates25


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview
import com.example.ingsw_24_25_dietiestates25.ui.theme.ComposeLoginScreenInitTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLoginScreenInitTheme {
            LoginScreen()

            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeLoginScreenInitTheme {
        LoginScreen()
    }
}