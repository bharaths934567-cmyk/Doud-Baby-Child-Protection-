package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object TelegramBot : Screen("telegram", "Telegram Bot", Icons.Default.Send)
    object AiSafety : Screen("ai_safety", "AI Safety", Icons.Default.Psychology)
    object Onboarding : Screen("onboarding", "Pairing Setup", Icons.Default.PersonAdd)
    object Vault : Screen("vault", "Security Vault", Icons.Default.Security)
}

val navItems = listOf(
    Screen.Dashboard,
    Screen.TelegramBot,
    Screen.AiSafety,
    Screen.Onboarding,
    Screen.Vault
)
