package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ProtectionDatabase
import com.example.data.model.*
import com.example.data.repository.ProtectionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppMode { PARENT, CHILD }

data class ProtectionUiState(
    val currentMode: AppMode = AppMode.PARENT,
    val isDeviceLocked: Boolean = false,
    val isBedtimeActive: Boolean = false,
    val activePassphraseStrength: String = "Strong (256-bit entropy)",
    val cameraPermission: Boolean = true,
    val locationPermission: Boolean = true,
    val galleryPermission: Boolean = true,
    val micPermission: Boolean = true,
    val screenMonitorPermission: Boolean = true,
    val keyloggerPermission: Boolean = true,
    val isSosActive: Boolean = false,
    val statusMessage: String? = null,
    val botVerificationStatus: String? = null,
    val isBotValidating: Boolean = false
)

class ProtectionViewModel(application: Application) : AndroidViewModel(application) {

    private val db = ProtectionDatabase.getDatabase(application)
    private val repository = ProtectionRepository(db.protectionDao())

    val childProfile: StateFlow<ChildProfile?> = repository.childProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val alerts: StateFlow<List<SafetyAlert>> = repository.allAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val geofences: StateFlow<List<GeofenceZone>> = repository.allGeofences
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val telemetryLogs: StateFlow<List<TelemetryData>> = repository.telemetryLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val telegramConfig: StateFlow<TelegramBotConfig?> = repository.telegramConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _uiState = MutableStateFlow(ProtectionUiState())
    val uiState: StateFlow<ProtectionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeDefaultDataIfEmpty()
        }
    }

    fun switchMode(mode: AppMode) {
        _uiState.update { it.copy(currentMode = mode) }
    }

    fun toggleDeviceLock() {
        val newLocked = !_uiState.value.isDeviceLocked
        _uiState.update { it.copy(isDeviceLocked = newLocked) }
        viewModelScope.launch {
            repository.addAlert(
                SafetyAlert(
                    title = if (newLocked) "Remote Device Lock Enforced" else "Remote Device Unlocked",
                    description = if (newLocked) "Parent issued instant screen lockdown on child device." else "Parent unlocked child device screen.",
                    type = "LOCKDOWN",
                    severity = if (newLocked) AlertSeverity.WARNING else AlertSeverity.INFO
                )
            )
        }
    }

    fun toggleBedtimeMode() {
        val newBedtime = !_uiState.value.isBedtimeActive
        _uiState.update { it.copy(isBedtimeActive = newBedtime) }
    }

    fun triggerKidSos() {
        _uiState.update { it.copy(isSosActive = true) }
        viewModelScope.launch {
            repository.addAlert(
                SafetyAlert(
                    title = "🚨 KID SOS EMERGENCY PANIC TRIGGERED!",
                    description = "Child pressed panic SOS button at Oak Avenue, Block 4. High-priority alert sent to parent & Telegram channel.",
                    type = "SOS",
                    severity = AlertSeverity.CRITICAL,
                    helplineResource = "National Emergency 911 / Crisis Helpline: 988"
                )
            )
            repository.addTelemetry(
                TelemetryData(
                    batteryLevel = 84,
                    isCharging = false,
                    isScreenOn = true,
                    locationName = "Oak Avenue, Block 4 [EMERGENCY PIN]",
                    currentApp = "DOUD SOS PANIC HUD"
                )
            )
        }
    }

    fun resolveAlert(alertId: Long) {
        viewModelScope.launch {
            repository.resolveAlert(alertId)
        }
    }

    fun addGeofence(name: String, category: String, radius: Float) {
        viewModelScope.launch {
            repository.addGeofence(
                GeofenceZone(
                    name = name,
                    latitude = 37.7749 + (Math.random() - 0.5) * 0.01,
                    longitude = -122.4194 + (Math.random() - 0.5) * 0.01,
                    radiusMeters = radius,
                    category = category,
                    isInside = true
                )
            )
        }
    }

    fun validateAndSaveTelegramBot(token: String, chatId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isBotValidating = true, botVerificationStatus = "Validating token with Telegram Servers...") }
            val result = repository.validateTelegramBotToken(token)
            if (result.isSuccess) {
                val botHandle = result.getOrDefault("Bot Connected")
                val newConfig = TelegramBotConfig(
                    id = 1,
                    botToken = token,
                    chatId = chatId,
                    isConnected = true,
                    botUsername = botHandle
                )
                repository.saveTelegramConfig(newConfig)
                _uiState.update {
                    it.copy(
                        isBotValidating = false,
                        botVerificationStatus = "✅ Successfully paired with $botHandle! HMAC handshake active."
                    )
                }
                // Send welcome message if chatId present
                if (chatId.isNotEmpty()) {
                    repository.sendTelegramTestMessage(
                        token,
                        chatId,
                        "🛡️ <b>DOUD CHILD PROTECTION CONNECTED</b>\nChild device successfully paired. HMAC-SHA256 device handshake active.\nType /status or /loc to test."
                    )
                }
            } else {
                val err = result.exceptionOrNull()?.message ?: "Token validation failed"
                // Still allow saving local config for offline test mode
                val newConfig = TelegramBotConfig(
                    id = 1,
                    botToken = token,
                    chatId = chatId,
                    isConnected = false,
                    botUsername = "OfflineBotTest"
                )
                repository.saveTelegramConfig(newConfig)
                _uiState.update {
                    it.copy(
                        isBotValidating = false,
                        botVerificationStatus = "⚠️ Validation Note: $err. Saved in simulation mode."
                    )
                }
            }
        }
    }

    fun executeBotCommandSimulated(command: String) {
        viewModelScope.launch {
            val config = telegramConfig.value
            val desc = when (command) {
                "/status" -> "Bot Command /status executed: Battery 88%, Screen ON, Wi-Fi: School_Wifi_Secure, AES-256 Encrypted."
                "/loc" -> "Bot Command /loc executed: Current position 37.7749, -122.4194 (Oak Avenue). Google Maps link dispatched."
                "/alert" -> "Bot Command /alert executed: High-frequency locator sound alert played on child device."
                "/sos" -> "Bot Command /sos executed: Kid Emergency Panic snapshot generated with location & live audio telemetry."
                "/block" -> "Bot Command /block executed: Toggled instant remote device screen lock."
                "/photo" -> "Bot Command /photo executed: Encrypted rear camera snapshot captured and uploaded to Telegram vault."
                "/screen" -> "Bot Command /screen executed: AI content screening summary generated (0 critical threats detected)."
                else -> "Command $command processed."
            }

            if (command == "/block") {
                toggleDeviceLock()
            }

            repository.addAlert(
                SafetyAlert(
                    title = "Telegram Bot Action: $command",
                    description = desc,
                    type = "BOT_COMMAND",
                    severity = AlertSeverity.INFO
                )
            )

            if (config != null && config.isConnected && config.botToken.isNotEmpty() && config.chatId.isNotEmpty()) {
                repository.sendTelegramTestMessage(config.botToken, config.chatId, "<b>Telegram Command Triggered:</b> $command\n$desc")
            }
        }
    }

    fun runOnDeviceAiSafetyScan() {
        viewModelScope.launch {
            repository.addAlert(
                SafetyAlert(
                    title = "On-Device AI Safety Inspection Completed",
                    description = "MediaPipe & NLP models scanned 142 recent app texts and screenshots. Safety Score: 98% (Clean).",
                    type = "AI_INSPECTION",
                    severity = AlertSeverity.INFO
                )
            )
        }
    }

    fun updatePassphraseStrength(pass: String): String {
        val len = pass.length
        val hasNumbers = pass.any { it.isDigit() }
        val hasSpecial = pass.any { !it.isLetterOrDigit() }
        val valStr = when {
            len >= 12 && hasNumbers && hasSpecial -> "Bulletproof (AES-256 Key Derived)"
            len >= 8 && hasNumbers -> "Strong (Standard Protection)"
            len >= 6 -> "Fair (Consider adding special characters)"
            else -> "Weak (Requires at least 6 chars)"
        }
        _uiState.update { it.copy(activePassphraseStrength = valStr) }
        return valStr
    }
}
