package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlertSeverity
import com.example.data.model.SafetyAlert
import com.example.ui.components.KidSosButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.ProtectionViewModel

@Composable
fun DashboardScreen(
    viewModel: ProtectionViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val childProfile by viewModel.childProfile.collectAsState()
    val alerts by viewModel.alerts.collectAsState()
    val geofences by viewModel.geofences.collectAsState()
    val telemetryLogs by viewModel.telemetryLogs.collectAsState()

    val latestTelemetry = telemetryLogs.firstOrNull()
    var showAddGeofenceDialog by remember { mutableStateOf(false) }
    var geofenceNameInput by remember { mutableStateOf("") }
    var geofenceCategoryInput by remember { mutableStateOf("SAFE") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IndigoDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Kid SOS Emergency Panic Button
        item {
            KidSosButton(
                onTriggerSos = { viewModel.triggerKidSos() },
                isSosActive = uiState.isSosActive
            )
        }

        // Live Telemetry Cards Grid
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, IndigoCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LIVE TELEMETRY: ${childProfile?.name ?: "Leo"}'s Device",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EmeraldSafe.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, EmeraldSafe)
                        ) {
                            Text(
                                text = "LIVE ONLINE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldSafe
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryCard(
                            icon = Icons.Default.BatteryChargingFull,
                            title = "Battery Level",
                            value = "${latestTelemetry?.batteryLevel ?: 88}%",
                            subtitle = if (latestTelemetry?.isCharging == true) "Charging" else "Discharging",
                            iconTint = EmeraldSafe,
                            modifier = Modifier.weight(1f)
                        )
                        TelemetryCard(
                            icon = Icons.Default.LocationOn,
                            title = "Current Location",
                            value = latestTelemetry?.locationName ?: "Oak Ave, Block 4",
                            subtitle = "37.7749, -122.4194",
                            iconTint = CyanAccent,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryCard(
                            icon = Icons.Default.Wifi,
                            title = "Wi-Fi Network",
                            value = latestTelemetry?.wifiSsid ?: "Home_5G_Protected",
                            subtitle = "TLS 1.3 Certified",
                            iconTint = ShieldGlow,
                            modifier = Modifier.weight(1f)
                        )
                        TelemetryCard(
                            icon = Icons.Default.Smartphone,
                            title = "Screen & App State",
                            value = if (uiState.isDeviceLocked) "LOCKED" else "ACTIVE",
                            subtitle = latestTelemetry?.currentApp ?: "Khan Academy",
                            iconTint = if (uiState.isDeviceLocked) RubyEmergency else EmeraldSafe,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Quick Remote Actions
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, IndigoCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "REMOTE GUARDIAN CONTROLS",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActionButton(
                            icon = if (uiState.isDeviceLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                            label = if (uiState.isDeviceLocked) "UNLOCK DEVICE" else "INSTANT LOCK",
                            isActive = uiState.isDeviceLocked,
                            activeColor = RubyEmergency,
                            onClick = { viewModel.toggleDeviceLock() },
                            modifier = Modifier.weight(1f).testTag("remote_lock_button")
                        )

                        ActionButton(
                            icon = Icons.Default.NightsStay,
                            label = if (uiState.isBedtimeActive) "BEDTIME ON" else "BEDTIME MODE",
                            isActive = uiState.isBedtimeActive,
                            activeColor = AmberWarning,
                            onClick = { viewModel.toggleBedtimeMode() },
                            modifier = Modifier.weight(1f).testTag("bedtime_mode_button")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActionButton(
                            icon = Icons.Default.VolumeUp,
                            label = "SOUND PING",
                            isActive = false,
                            activeColor = CyanAccent,
                            onClick = { viewModel.executeBotCommandSimulated("/alert") },
                            modifier = Modifier.weight(1f).testTag("sound_ping_button")
                        )

                        ActionButton(
                            icon = Icons.Default.PhotoCamera,
                            label = "SNAPSHOT",
                            isActive = false,
                            activeColor = CyanAccent,
                            onClick = { viewModel.executeBotCommandSimulated("/photo") },
                            modifier = Modifier.weight(1f).testTag("camera_snapshot_button")
                        )
                    }
                }
            }
        }

        // Geofences Overview
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, IndigoCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "GEOFENCE BOUNDARIES",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                        )
                        IconButton(onClick = { showAddGeofenceDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Add Geofence", tint = CyanAccent)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    geofences.forEach { fence ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (fence.isInside) EmeraldSafe else RubyEmergency)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = fence.name,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimaryDark
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Radius: ${fence.radiusMeters.toInt()}m (${if (fence.isInside) "Inside" else "Outside"})",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (fence.isInside) EmeraldSafe else RubyEmergency
                                )
                            )
                        }
                    }
                }
            }
        }

        // Safety Alerts Feed
        item {
            Text(
                text = "SAFETY ALERTS & INCIDENT AUDIT TRAIL",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            )
        }

        items(alerts) { alert ->
            SafetyAlertCard(
                alert = alert,
                onResolve = { viewModel.resolveAlert(alert.id) }
            )
        }
    }

    if (showAddGeofenceDialog) {
        AlertDialog(
            onDismissRequest = { showAddGeofenceDialog = false },
            title = { Text("Add Protected Geofence", color = TextPrimaryDark) },
            text = {
                Column {
                    OutlinedTextField(
                        value = geofenceNameInput,
                        onValueChange = { geofenceNameInput = it },
                        label = { Text("Geofence Name (e.g. Grandma's House)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = geofenceCategoryInput,
                        onValueChange = { geofenceCategoryInput = it },
                        label = { Text("Category (SAFE / SCHOOL / RESTRICTED)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (geofenceNameInput.isNotEmpty()) {
                            viewModel.addGeofence(geofenceNameInput, geofenceCategoryInput, 250f)
                            showAddGeofenceDialog = false
                            geofenceNameInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                ) {
                    Text("Add Zone")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddGeofenceDialog = false }) {
                    Text("Cancel", color = TextSecondaryDark)
                }
            },
            containerColor = IndigoSurface
        )
    }
}

@Composable
private fun TelemetryCard(
    icon: ImageVector,
    title: String,
    value: String,
    subtitle: String,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = IndigoCard
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryDark)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondaryDark,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) activeColor else IndigoCard,
            contentColor = if (isActive) Color.White else TextPrimaryDark
        ),
        border = BorderStroke(1.dp, if (isActive) activeColor else IndigoCard),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun SafetyAlertCard(
    alert: SafetyAlert,
    onResolve: () -> Unit
) {
    val severityColor = when (alert.severity) {
        AlertSeverity.CRITICAL -> RubyEmergency
        AlertSeverity.WARNING -> AmberWarning
        AlertSeverity.INFO -> CyanAccent
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = IndigoSurface),
        border = BorderStroke(1.dp, if (alert.isResolved) IndigoCard else severityColor.copy(alpha = 0.6f)),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (alert.isResolved) TextSecondaryDark else severityColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = alert.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (alert.isResolved) TextSecondaryDark else TextPrimaryDark
                        )
                    )
                }
                if (!alert.isResolved) {
                    TextButton(onClick = onResolve) {
                        Text("Resolve", style = MaterialTheme.typography.labelSmall.copy(color = EmeraldSafe))
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = alert.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (alert.isResolved) TextSecondaryDark else TextPrimaryDark,
                    fontSize = 12.sp
                )
            )

            if (!alert.helplineResource.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = RubyEmergency.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, RubyEmergency)
                ) {
                    Text(
                        text = "🚨 Crisis Resource: ${alert.helplineResource}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = RubyEmergency,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
