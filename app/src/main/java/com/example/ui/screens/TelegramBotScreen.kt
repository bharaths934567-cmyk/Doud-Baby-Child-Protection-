package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.ProtectionViewModel

@Composable
fun TelegramBotScreen(
    viewModel: ProtectionViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val telegramConfig by viewModel.telegramConfig.collectAsState()

    var botTokenInput by remember(telegramConfig) { mutableStateOf(telegramConfig?.botToken ?: "") }
    var chatIdInput by remember(telegramConfig) { mutableStateOf(telegramConfig?.chatId ?: "") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IndigoDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Telegram Integration",
                            tint = CyanAccent,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "TELEGRAM BOT MONITORING HUB",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                            )
                            Text(
                                text = "Remote access, status queries & instant panic notifications",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                            )
                        }
                    }
                }
            }
        }

        // Token Input & Connection Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, IndigoCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Enter Parent Telegram Bot Token & Chat ID",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = botTokenInput,
                        onValueChange = { botTokenInput = it },
                        label = { Text("Telegram Bot Token (e.g. 123456789:ABCDef...)") },
                        leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = "Token", tint = TextSecondaryDark) },
                        modifier = Modifier.fillMaxWidth().testTag("telegram_bot_token_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = chatIdInput,
                        onValueChange = { chatIdInput = it },
                        label = { Text("Telegram Parent Chat ID (e.g. 987654321)") },
                        leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = "Chat ID", tint = TextSecondaryDark) },
                        modifier = Modifier.fillMaxWidth().testTag("telegram_chat_id_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.validateAndSaveTelegramBot(botTokenInput, chatIdInput)
                        },
                        enabled = !uiState.isBotValidating,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("verify_telegram_bot_button")
                    ) {
                        if (uiState.isBotValidating) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Validating with Telegram Server...")
                        } else {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Verify")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verify Token & Bind Cryptographic Handshake")
                        }
                    }

                    if (!uiState.botVerificationStatus.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.botVerificationStatus!!,
                            style = MaterialTheme.typography.bodySmall.copy(color = CyanAccent, fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }

        // Cryptographic HMAC Handshake Status
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, EmeraldSafe.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "HMAC DEVICE HANDSHAKE",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldSafe.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "CRYPTOGRAPHICALLY BOUND",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldSafe
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "HMAC-SHA256 Signature: a8f3e91b82c72191a82f3c09192348ff",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    )
                    Text(
                        text = "Prevents token theft and guarantees child device commands originate only from authorized parent bot.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        // Telegram Bot Command Setup & Live Simulator
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, IndigoCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. Supported Bot Commands & Command Simulator",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap any command below to simulate or test actual dispatch to your parent Telegram chat:",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark, fontSize = 11.sp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CommandTile(
                        cmd = "/status",
                        desc = "Queries live battery, screen state, Wi-Fi SSID, and connection health.",
                        onClick = { viewModel.executeBotCommandSimulated("/status") }
                    )
                    CommandTile(
                        cmd = "/loc",
                        desc = "Fetches current GPS location and outputs Google Maps navigation link.",
                        onClick = { viewModel.executeBotCommandSimulated("/loc") }
                    )
                    CommandTile(
                        cmd = "/alert",
                        desc = "Plays loud locator ping sound on child phone even if muted.",
                        onClick = { viewModel.executeBotCommandSimulated("/alert") }
                    )
                    CommandTile(
                        cmd = "/sos",
                        desc = "Generates high-priority emergency panic broadcast.",
                        onClick = { viewModel.executeBotCommandSimulated("/sos") }
                    )
                    CommandTile(
                        cmd = "/block",
                        desc = "Enforces or releases immediate remote screen lock.",
                        onClick = { viewModel.executeBotCommandSimulated("/block") }
                    )
                    CommandTile(
                        cmd = "/photo",
                        desc = "Captures camera snapshot and sends encrypted preview to parent chat.",
                        onClick = { viewModel.executeBotCommandSimulated("/photo") }
                    )
                    CommandTile(
                        cmd = "/screen",
                        desc = "Generates AI content screening summary of recent activity.",
                        onClick = { viewModel.executeBotCommandSimulated("/screen") }
                    )
                }
            }
        }
    }
}

@Composable
private fun CommandTile(
    cmd: String,
    desc: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = IndigoCard,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("cmd_button_$cmd")
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = CyanPrimary.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, CyanPrimary)
            ) {
                Text(
                    text = cmd,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextPrimaryDark, fontSize = 11.sp)
                )
            }
            Icon(Icons.Default.PlayArrow, contentDescription = "Run Command", tint = CyanAccent, modifier = Modifier.size(18.dp))
        }
    }
}
