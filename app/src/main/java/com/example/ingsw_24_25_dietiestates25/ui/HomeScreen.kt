package com.example.ingsw_24_25_dietiestates25.ui


import android.net.Uri
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.HomeViewModel
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.profileUI.ProfileViewModel
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient
import com.example.ingsw_24_25_dietiestates25.ui.theme.DietiEstatesTheme
import com.example.ingsw_24_25_dietiestates25.ui.theme.testColor
import com.example.ingsw_24_25_dietiestates25.ui.utils.*

@Composable
fun HomeScreen(
    navController: NavController,
    hm: HomeViewModel
) {
    var selectedSegment by remember { mutableStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val state by hm.state.collectAsState()

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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Icona personalizzata",
                tint = Color.Unspecified,
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text(
                    text = "What do you need?",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SegmentedControl(
                options = listOf("Rent", "Buy"),
                selectedIndex = selectedSegment,
                onOptionSelected = { index ->
                    selectedSegment = index
                    val type = if (index == 0) "Rent" else "Buy"
                    hm.onPropertyTypeSelected(type)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text(
                    text = "Where ?",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp),
                    textAlign = TextAlign.Start
                )
            }

            OutlinedTextField(
                value = state.location,
                onValueChange = { hm.onLocationChanged(it) },
                label = { Text("Enter location") },
                singleLine = true,
                isError = state.errorMessage != null,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(70.dp))

            Button(
                onClick = {
                    val result = hm.onSearchClicked()
                    if (result != null) {
                        val (type, location) = result
                        val safeLocation = Uri.encode(location)
                        navController.navigate("RESULTS?type=$type&location=$safeLocation")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(FiftySixDp)
            ) {
                Text(
                    text = "SEARCH",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 24.sp
                    ),
                    color = Color.White
                )
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
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = Modifier
            .background(
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(50)
            )
            .height(FortyDp)
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
                    .clickable {
                        onOptionSelected(index)
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                    .background(
                        color = if (isSelected) testColor else Color.Transparent,
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    ),
                    color = if (isSelected) Color.White else Color.Black
                )
            }
        }
    }
}

private val FiftySixDp = 56.dp
private val FortyDp = 40.dp

