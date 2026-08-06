package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SupervisedHeaderBanner
import com.example.ui.navigation.Screen
import com.example.ui.navigation.navItems
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ProtectionViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ProtectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DoudProtectionTheme {
                val uiState by viewModel.uiState.collectAsState()
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(IndigoDark),
                    topBar = {
                        SupervisedHeaderBanner(
                            currentMode = uiState.currentMode,
                            onSwitchModeRequested = { newMode ->
                                viewModel.switchMode(newMode)
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = PolishSurface,
                            contentColor = TextPrimaryDark,
                            tonalElevation = 4.dp,
                            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                        ) {
                            navItems.forEach { screen ->
                                val selected = currentScreen.route == screen.route
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { currentScreen = screen },
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = screen.title,
                                            tint = if (selected) BluePrimary else TextSecondaryDark
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = screen.title,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (selected) BluePrimary else TextSecondaryDark
                                            )
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = BlueContainer
                                    ),
                                    modifier = Modifier.testTag("nav_item_${screen.route}")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Crossfade(
                            targetState = currentScreen,
                            label = "ScreenTransition"
                        ) { screen ->
                            when (screen) {
                                Screen.Dashboard -> DashboardScreen(viewModel = viewModel)
                                Screen.TelegramBot -> TelegramBotScreen(viewModel = viewModel)
                                Screen.AiSafety -> AiSafetyScreen(viewModel = viewModel)
                                Screen.Onboarding -> OnboardingScreen(viewModel = viewModel)
                                Screen.Vault -> SecurityVaultScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
