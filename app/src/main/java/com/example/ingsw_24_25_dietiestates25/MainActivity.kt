package com.example.ingsw_24_25_dietiestates25

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ingsw_24_25_dietiestates25.ui.social.FacebookButton
import com.example.ingsw_24_25_dietiestates25.ui.theme.ComposeLoginScreenInitTheme
import com.facebook.AccessToken


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mostra il FacebookButton
        setContent {
            ComposeLoginScreenInitTheme {
                MainScreen()
            }
        }
    }
}



