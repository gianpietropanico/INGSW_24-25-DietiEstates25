package com.example.ingsw_24_25_dietiestates25.ui
import com.example.ingsw_24_25_dietiestates25.ui.viewmodels.AgencyViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AgencyRegistrationScreen(
    agencyViewModel: AgencyViewModel = viewModel()
) {
    val agencyName by agencyViewModel.agencyName.collectAsState()
    val email by agencyViewModel.email.collectAsState()
    val isLoading by agencyViewModel.isLoading.collectAsState()
    val errorMessage by agencyViewModel.errorMessage.collectAsState()
    val isRegistrationSuccessful by agencyViewModel.isRegistrationSuccessful.collectAsState()

    if (isRegistrationSuccessful) {
        Text(
            text = "Registration completed successfully!",
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h6
        )
    } else {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Register Real Estate Agency",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = agencyName,
                onValueChange = agencyViewModel::updateAgencyName,
                label = { Text("Agency Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = agencyViewModel::updateEmail,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.body2
                )
            }

            Button(
                onClick = agencyViewModel::registerAgency,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colors.onPrimary
                    )
                } else {
                    Text("Register")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {
        AgencyRegistrationScreen()
    }
}
