package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ChildProfile
import com.example.ui.components.PassphraseStrengthMeter
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppMode
import com.example.ui.viewmodel.ProtectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: ProtectionViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val childProfile by viewModel.childProfile.collectAsState()

    var parentEmail by remember { mutableStateOf("parent.guardian@doudprotection.org") }
    var parentPassphrase by remember { mutableStateOf("DoudSecured#2026") }
    var childName by remember { mutableStateOf(childProfile?.name ?: "Leo") }
    var selectedAgeGroup by remember { mutableStateOf("10-12 years (Supervised)") }
    var selectedPlatform by remember { mutableStateOf("Android 14 (Full Suite)") }
    var showSuccessToast by remember { mutableStateOf(false) }

    val strengthText = remember(parentPassphrase) {
        viewModel.updatePassphraseStrength(parentPassphrase)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IndigoDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_doud_hero_1785994586583),
                            contentDescription = "Doud Protection Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(IndigoDark.copy(alpha = 0.5f))
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "DOUD CHILD PROTECTION",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = CyanAccent
                                )
                            )
                            Text(
                                text = "Next-Gen Parental Supervision & Telemetry Vault",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextPrimaryDark)
                            )
                        }
                    }
                }
            }
        }

        // 1. Parent Account Registration & Passphrase
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, IndigoCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Parent Security",
                            tint = CyanAccent
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "1. Parent Registration & Passphrase",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = parentEmail,
                        onValueChange = { parentEmail = it },
                        label = { Text("Parent Guardian Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = TextSecondaryDark) },
                        modifier = Modifier.fillMaxWidth().testTag("parent_email_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = parentPassphrase,
                        onValueChange = { parentPassphrase = it },
                        label = { Text("Parent Master Passphrase") },
                        leadingIcon = { Icon(Icons.Default.Key, contentDescription = "Key", tint = TextSecondaryDark) },
                        modifier = Modifier.fillMaxWidth().testTag("parent_passphrase_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PassphraseStrengthMeter(strengthText = strengthText)
                }
            }
        }

        // 2. Add Child Wizard
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, IndigoCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Child Setup",
                            tint = CyanAccent
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "2. Add Child Profile Wizard",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = childName,
                        onValueChange = { childName = it },
                        label = { Text("Child Name") },
                        leadingIcon = { Icon(Icons.Default.Face, contentDescription = "Child Face", tint = TextSecondaryDark) },
                        modifier = Modifier.fillMaxWidth().testTag("child_name_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedAgeGroup,
                            onValueChange = { selectedAgeGroup = it },
                            label = { Text("Age Bracket") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = selectedPlatform,
                            onValueChange = { selectedPlatform = it },
                            label = { Text("Device OS") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.switchMode(AppMode.PARENT)
                            showSuccessToast = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("save_child_profile_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save Profile")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save & Generate Child Pairing Token")
                    }

                    if (showSuccessToast) {
                        Text(
                            text = "✅ Child profile updated! HMAC Pairing Token generated.",
                            style = MaterialTheme.typography.labelSmall.copy(color = EmeraldSafe),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        // 3. Pairing QR Code & HMAC Handshake Token
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "3. Child Device Pairing QR Code",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Scan from child's device camera to initiate cryptographic HMAC handshake",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // QR Code visual simulator
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = "Pairing QR",
                            tint = IndigoDark,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = IndigoCard
                    ) {
                        Text(
                            text = "Token: DOUD-8849-SECURE-HMAC256",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // 4. Required Permission Checklist
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSurface),
                border = BorderStroke(1.dp, IndigoCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "4. Child Permission Checklist & Transparency",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Plain language disclosure for guardian transparency and Play Store compliance:",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PermissionItem(
                        icon = Icons.Default.CameraAlt,
                        title = "Camera Access",
                        description = "Enables active remote snapshot preview only during parental monitoring. Never background recorded.",
                        isGranted = uiState.cameraPermission
                    )
                    PermissionItem(
                        icon = Icons.Default.MyLocation,
                        title = "Live Location & Geofencing",
                        description = "Provides continuous GPS coordinates to alert parents if child exits safe home or school boundaries.",
                        isGranted = uiState.locationPermission
                    )
                    PermissionItem(
                        icon = Icons.Default.PhotoLibrary,
                        title = "Gallery Photo Inspection",
                        description = "On-device AI screens photo media for safety threats (NSFW/Violence) before local encryption.",
                        isGranted = uiState.galleryPermission
                    )
                    PermissionItem(
                        icon = Icons.Default.Mic,
                        title = "Live Audio Snapshot",
                        description = "Allows emergency 10-second audio stream during Kid SOS trigger.",
                        isGranted = uiState.micPermission
                    )
                    PermissionItem(
                        icon = Icons.Default.ScreenShare,
                        title = "Screen Projection & Keyloggers",
                        description = "Accessibility Service & MediaProjection disclosure: Used strictly for cyberbullying prevention.",
                        isGranted = uiState.screenMonitorPermission
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionItem(
    icon: ImageVector,
    title: String,
    description: String,
    isGranted: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isGranted) EmeraldSafe.copy(alpha = 0.2f) else RubyEmergency.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isGranted) EmeraldSafe else RubyEmergency,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                )
                Text(
                    text = if (isGranted) "Granted" else "Requires Grant",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isGranted) EmeraldSafe else RubyEmergency,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondaryDark,
                    fontSize = 11.sp
                )
            )
        }
    }
}
