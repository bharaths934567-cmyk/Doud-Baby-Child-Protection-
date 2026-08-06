package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = BlueContainer,
    onPrimaryContainer = BlueContainerText,
    secondary = BluePrimary,
    onSecondary = Color.White,
    background = PolishBackground,
    onBackground = TextPrimaryDark,
    surface = PolishSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = PolishSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    error = RubyEmergency,
    onError = Color.White
)

@Composable
fun DoudProtectionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

