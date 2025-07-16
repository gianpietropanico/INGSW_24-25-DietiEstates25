package com.example.ingsw_24_25_dietiestates25.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.theme.DietiEstatesTheme
import com.example.ingsw_24_25_dietiestates25.ui.utils.*


@Composable
fun HomeScreen(
    navController : NavController
) {
    var selectedSegment by remember { mutableStateOf(0) }
    var propertyType by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            DietiNavBar(
                currentRoute = currentRoute ?: Screen.Home.route,
                onRouteSelected = { newRoute ->
                    navController.navigate(newRoute) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp) ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter    = painterResource(id = R.drawable.logo),
                contentDescription = "Icona personalizzata",
                tint       = Color.Unspecified,
                modifier   = Modifier
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            SegmentedControl(
                options = listOf("Rent", "Buy"),
                selectedIndex = selectedSegment,
                onOptionSelected = { selectedSegment = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = propertyType,
                onValueChange = { propertyType = it },
                leadingIcon = {
                    Icon(
                        imageVector =   ImageVector.vectorResource(id = R.drawable.home_icon),
                        contentDescription = null
                    )
                },
                placeholder = { Text("Casa e appartamenti") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)), // <-- Applica corner visivo
                shape = RoundedCornerShape(16.dp), // <-- Applica alla struttura del field
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                placeholder = { Text("Dove vuoi cercare?") },
                leadingIcon = {
                    Icon(
                        imageVector =   ImageVector.vectorResource(id = R.drawable.location_icon),
                        contentDescription = null
                    )
                },
                modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)), // <-- Applica corner visivo
                shape = RoundedCornerShape(16.dp), // <-- Applica alla struttura del field
            )

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { /* TODO: Launch search with parameters */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height( FiftySixDp )
            ) {
                Text(
                    text = "SEARCH",
                    style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                ))
            }
        }
    }
}

@Composable
fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(50)
            )
            .height( FortyDp )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = index == selectedIndex
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onOptionSelected(index) }
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    color = if (isSelected) Color.White else Color.Black
                )
            }
        }
    }
}


private val FiftySixDp = 56.dp
private val FortyDp = 40.dp

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun HomeScreenPreview() {
    DietiEstatesTheme {
        HomeScreen(navController = rememberNavController())
    }
}