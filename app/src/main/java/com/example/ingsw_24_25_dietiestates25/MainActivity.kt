package com.example.ingsw_24_25_dietiestates25

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.example.ingsw_24_25_dietiestates25.ui.theme.ComposeLoginScreenInitTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLoginScreenInitTheme {
                MainScreen()
            }
        }
    }
}

