package com.example.ingsw_24_25_dietiestates25.ui.authUI.socialbutton

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.R


@Composable
fun SocialLoginSection(
    isLoading: Boolean,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onGithubClick: () -> Unit,
    iconSize: Dp = 40.dp,
    spacing: Dp = 24.dp
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Divider con testo al centro
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "OR USE",
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        // Bottoni social
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            SocialIconButton(
                iconRes = R.drawable.google_icon,
                contentDescription = "Google",
                size = iconSize,
                enabled = !isLoading,
                onClick = onGoogleClick
            )

            SocialIconButton(
                iconRes = R.drawable.facebook_icon,
                contentDescription = "Facebook",
                size = iconSize,
                enabled = !isLoading,
                onClick = onFacebookClick
            )

            SocialIconButton(
                iconRes = R.drawable.github,
                contentDescription = "GitHub",
                size = iconSize,
                enabled = !isLoading,
                onClick = onGithubClick
            )
        }
    }
}

@Composable
fun SocialIconButton(
    iconRes: Int,
    contentDescription: String,
    size: Dp,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(
                enabled = enabled,
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = Color.Unspecified,
            modifier = Modifier.fillMaxSize()
        )
    }
}