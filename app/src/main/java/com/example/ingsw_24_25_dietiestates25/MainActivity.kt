
package com.example.ingsw_24_25_dietiestates25
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ingsw_24_25_dietiestates25.ui.navigation.AppNavHost
import com.example.ingsw_24_25_dietiestates25.ui.theme.DietiEstatesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DietiEstatesTheme {
                AppNavHost()
            }
        }
    }
}

