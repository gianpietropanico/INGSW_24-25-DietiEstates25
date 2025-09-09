package com.example.ingsw_24_25_dietiestates25.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text

import androidx.compose.ui.Alignment
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed


@Composable
fun MinimalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector? = null,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    textStyle: TextStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
    underlineColor: Color = Color(0xFF3A7CA5),
    labelColor: Color = Color.DarkGray,
    iconTint: Color = Color.Gray,
    onError: Boolean
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 14.sp,
                color = if (onError) DarkRed else labelColor
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (onError) DarkRed else iconTint,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }


            Box(modifier = Modifier.fillMaxWidth()) {
                if (value.isEmpty() && placeholder != null) {
                    Text(
                        text = placeholder,
                        style = textStyle.copy(if (onError) DarkRed else Color.Gray)
                    )
                }

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = textStyle.copy(if (onError) DarkRed else Color.Gray),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = if (onError) DarkRed else underlineColor
        )
    }
}


//Text(
//text = "DietiEstates25",
//color = MaterialTheme.colorScheme.onBackground,
//style = MaterialTheme.typography.headlineLarge,
//modifier = Modifier
//.wrapContentWidth()
//.drawBehind {
//    val stroke = 2.dp.toPx()
//    val padding = 16.dp.toPx()
//    val y = size.height + stroke / 2
//
//    drawLine(
//        color = underlineColor,
//        start = Offset(padding, y),
//        end = Offset(size.width - padding, y),
//        strokeWidth = stroke
//    )
//}
//)