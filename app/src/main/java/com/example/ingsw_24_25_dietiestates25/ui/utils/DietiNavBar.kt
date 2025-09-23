package com.example.ingsw_24_25_dietiestates25.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ingsw_24_25_dietiestates25.ui.theme.BlueGray
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu
import com.example.ingsw_24_25_dietiestates25.R

/*
COME FUNZIONA LA NAVIGAZIONE NELLA NAVBAR
GLI ITEMS INDICANO LE ROUTE CHE LA NAVIGATION SEGUE, QUESTI ITEM SONO PRESENTI NELLA SEALED CLASS SCREEN
NEI PARAMETRI DI QUEI OGETTI C'è ROUTE CHE SERVE A FAR CAPIRE ALL'APP NAVHOST DOVE DEVE ANDARE
SI NOTA CHE SONO SCRITTI IN MINUSCOLO POICHE C'è UNA FUNZIONE CHE VA A TROVARE LA ROUTE NELLA NAVHOST E
USA LO RENDE MINUSCOLO COSI CHE VIENE INDIRIZZATO LI
 */
@Composable
fun DietiNavBar(
    currentRoute: String,
    onRouteSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    items: List<Screen> = listOf(Screen.Inbox, Screen.Home, Screen.Profile),
) {
    val selectedColor = primaryBlu
    val unselectedColor = BlueGray

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        tonalElevation = 0.dp
    ) {
        items.forEach { screen ->
            val selected = currentRoute.equals(screen.route, ignoreCase = true)

            NavigationBarItem(
                selected = selected,
                onClick = { onRouteSelected(screen.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = screen.label,
                        modifier = Modifier.size(32.dp),
                        tint = if (selected) selectedColor else unselectedColor
                    )
                },
                label = {
                    Text(
                        text = screen.label,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (selected) selectedColor else unselectedColor
//                            shadow = Shadow(
//                                color = if (selected) selectedColor else unselectedColor,
//                                blurRadius = 4f
//                            )
                        ),
                        textAlign = TextAlign.Center
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    selectedTextColor = selectedColor,
                    unselectedTextColor = unselectedColor
                )
            )
        }
    }
}

sealed class Screen(val route: String, val icon: Int, val label: String) {
    object Inbox     : Screen("inbox",     R.drawable.notification,     "INBOX")
    object Home      : Screen("home",   R.drawable.home,   "HOME")
    object Profile   : Screen("profile", R.drawable.profile_icon, "PROFILE")
}