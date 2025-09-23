package com.example.ingsw_24_25_dietiestates25.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ResultsViewModel
import com.example.ingsw_24_25_dietiestates25.ui.theme.testColor
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    navController: NavController,
    type: String,
    location: String,
    rm: ResultsViewModel
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        containerColor = Color.White,
        sheetContainerColor = Color.White,
        sheetContent = {
            FiltersSheet(
                onApply = {
                    scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                },
                onDismiss = {
                    scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                }
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Search results",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = testColor,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp) // spazio dai bordi
                            .size(32.dp) // area clic più grande
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.frecciaback),
                            contentDescription = "Back",
                            tint = testColor,
                            modifier = Modifier.size(20.dp) // freccia più piccola dentro
                        )
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier.padding(end = 16.dp, top = 8.dp, bottom = 8.dp), // spazio laterale e verticale
                        horizontalArrangement = Arrangement.spacedBy(12.dp) // spazio tra icone
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch { scaffoldState.bottomSheetState.expand() }
                            },
                            modifier = Modifier.size(32.dp) // area clic più grande
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = "Filters",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(40.dp) // icona interna più grande
                            )
                        }
                        IconButton(
                            onClick = { /* TODO: Mappa */ },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_map),
                                contentDescription = "Map",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "Found 0 estates",
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Composable
fun FiltersSheet(
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Filters",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("• Prezzo minimo/massimo", color = Color.Black)
        Text("• Numero stanze", color = Color.Black)
        Text("• Tipo di proprietà", color = Color.Black)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) { Text("Close") }

            Button(
                onClick = onApply,
                modifier = Modifier.weight(1f)
            ) { Text("Apply") }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ResultsScreenPreview() {
    val navController = rememberNavController()
    ResultsScreen(
        navController = navController,
        type = "Rent",
        location = "Napoli",
        rm = ResultsViewModel(
            propertyRepo = TODO()
        )
    )
}