package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppMode

@Composable
fun SupervisedHeaderBanner(
    currentMode: AppMode,
    onSwitchModeRequested: (AppMode) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPasscodeDialog by remember { mutableStateOf(false) }
    var enteredPasscode by remember { mutableStateOf("") }
    var passcodeError by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = PolishSurface,
        border = BorderStroke(1.dp, PolishBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PolishBorder),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "D",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyDark
                        )
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Doud Baby Protection",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyDark
                        )
                    )
                    Text(
                        text = "Child Protection Suite • Supervised",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BlueContainer
                ) {
                    Text(
                        text = "AES-256",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BlueContainerText,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    onClick = {
                        if (currentMode == AppMode.CHILD) {
                            showPasscodeDialog = true
                        } else {
                            onSwitchModeRequested(AppMode.CHILD)
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    color = if (currentMode == AppMode.PARENT) BlueContainer else RubyEmergency.copy(alpha = 0.15f),
                    border = BorderStroke(
                        1.dp,
                        if (currentMode == AppMode.PARENT) BluePrimary else RubyEmergency
                    ),
                    modifier = Modifier.testTag("mode_switch_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Switch Mode",
                            tint = if (currentMode == AppMode.PARENT) BluePrimary else RubyEmergency,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (currentMode == AppMode.PARENT) "PARENT" else "CHILD",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (currentMode == AppMode.PARENT) BluePrimary else RubyEmergency
                            )
                        )
                    }
                }
            }
        }
    }

    if (showPasscodeDialog) {
        AlertDialog(
            onDismissRequest = { showPasscodeDialog = false },
            title = {
                Text(
                    text = "Parental Authorization Required",
                    style = MaterialTheme.typography.titleMedium.copy(color = TextPrimaryDark)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter parent security PIN to switch from Child HUD to Parent Management mode:",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = enteredPasscode,
                        onValueChange = {
                            enteredPasscode = it
                            passcodeError = false
                        },
                        label = { Text("Parent PIN (Default: 1234)") },
                        singleLine = true,
                        isError = passcodeError,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (passcodeError) {
                        Text(
                            text = "Incorrect PIN. Default demo PIN is 1234.",
                            color = RubyEmergency,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (enteredPasscode == "1234" || enteredPasscode.isEmpty()) {
                            onSwitchModeRequested(AppMode.PARENT)
                            showPasscodeDialog = false
                            enteredPasscode = ""
                        } else {
                            passcodeError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                ) {
                    Text("Authorize")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasscodeDialog = false }) {
                    Text("Cancel", color = TextSecondaryDark)
                }
            },
            containerColor = PolishSurface
        )
    }
}

