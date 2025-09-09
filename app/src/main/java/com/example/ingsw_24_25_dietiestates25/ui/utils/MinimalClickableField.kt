package com.example.ingsw_24_25_dietiestates25.ui.utils
import androidx.compose.foundation.LocalIndication
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed


@Composable
fun MinimalClickableField(
    label: String,
    value: String,
    placeholder: String? = null,
    onEditIconClick: () -> Unit,
    showError: Boolean = false,
    modifier: Modifier = Modifier
) {
    var iconClicked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 13.sp,
                color = if (showError) DarkRed else Color.DarkGray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (value.isNotEmpty()) value else (placeholder ?: ""),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = when {
                        showError -> DarkRed
                        value.isEmpty() -> Color.Gray
                        else -> Color.Black
                    }
                )
            )

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = if (iconClicked || showError) Color.Black else Color.Gray,
                modifier = Modifier
                    .size(18.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = LocalIndication.current
                    ) {
                        iconClicked = true
                        onEditIconClick()
                    }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = Color.LightGray
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileClickableTextItemPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        MinimalClickableField(
            label = "Full Name",
            value = "Giuseppe Reitano",
            onEditIconClick = { println("Matita cliccata") }
        )
        Spacer(modifier = Modifier.height(24.dp))

        MinimalClickableField(
            label = "Email",
            value = "",
            placeholder = "Add your email",
            onEditIconClick = { println("Matita cliccata") }
        )
    }
}
