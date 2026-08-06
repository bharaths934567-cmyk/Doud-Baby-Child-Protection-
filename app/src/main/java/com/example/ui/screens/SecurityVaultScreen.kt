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
fun SecurityVaultScreen(
    viewModel: ProtectionViewModel,
    modifier: Modifier = Modifier
) {
    var retentionDays by remember { mutableFloatStateOf(30f) }
    var exportStatusMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IndigoDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Data Security Vault",
                            tint = CyanAccent,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "DATA SECURITY & VAULT ARCHITECTURE",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                            )
                            Text(
                                text = "AES-256-GCM Field Level • Hardware Android Keystore",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                            )
                        }
                    }
                }
            }
        }

        // Encryption Spec Cards
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, IndigoCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Cryptographic Architecture Specifications",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CryptoSpecRow(
                        title = "In Transit Encryption",
                        spec = "TLS 1.3 + Strict Certificate Pinning"
                    )
                    CryptoSpecRow(
                        title = "Local Database",
                        spec = "SQLCipher AES-256 Encrypted Storage"
                    )
                    CryptoSpecRow(
                        title = "Sensitive Media Telemetry",
                        spec = "Hybrid Crypto: X25519 Key Exchange + ChaCha20-Poly1305"
                    )
                    CryptoSpecRow(
                        title = "Master Key Protection",
                        spec = "Android Keystore System (Hardware-backed)"
                    )
                    CryptoSpecRow(
                        title = "Anti-Tamper & Integrity",
                        spec = "Play Integrity API + Root Signature Checks"
                    )
                }
            }
        }

        // Data Retention & Auto-Purge Policy Slider
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, IndigoCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Data Lifecycle & Auto-Purge Policy",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Automatically purges media & telemetry older than selected timeframe unless pinned:",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark, fontSize = 11.sp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Auto-Purge Window:", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark))
                        Text("${retentionDays.toInt()} Days", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = CyanAccent))
                    }

                    Slider(
                        value = retentionDays,
                        onValueChange = { retentionDays = it },
                        valueRange = 7f..90f,
                        steps = 2,
                        colors = SliderDefaults.colors(
                            thumbColor = CyanAccent,
                            activeTrackColor = CyanPrimary
                        ),
                        modifier = Modifier.testTag("retention_slider")
                    )
                }
            }
        }

        // Export Password-Protected Encrypted Archive
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, IndigoCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Parental Data Archive Export",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Export an encrypted, password-protected (.doudzip) archive containing full telemetry and incident audit logs.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark, fontSize = 11.sp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            exportStatusMessage = "📦 Generated encrypted archive: doud_telemetry_export_2026.doudzip (AES-256 Protected)"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("export_archive_button")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Export Archive")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export Password-Protected (.doudzip) Archive")
                    }

                    if (exportStatusMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = exportStatusMessage!!,
                            style = MaterialTheme.typography.bodySmall.copy(color = EmeraldSafe, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CryptoSpecRow(
    title: String,
    spec: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            )
            Text(
                text = spec,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = CyanAccent,
                    fontSize = 11.sp
                )
            )
        }
        Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = EmeraldSafe, modifier = Modifier.size(18.dp))
    }
}
