package com.example.data.repository

import com.example.data.local.ProtectionDao
import com.example.data.model.*
import com.example.data.telegram.TelegramService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ProtectionRepository(
    private val dao: ProtectionDao,
    private val telegramService: TelegramService = TelegramService()
) {

    val childProfile: Flow<ChildProfile?> = dao.getChildProfile()
    val allAlerts: Flow<List<SafetyAlert>> = dao.getAllAlerts()
    val allGeofences: Flow<List<GeofenceZone>> = dao.getAllGeofences()
    val telemetryLogs: Flow<List<TelemetryData>> = dao.getTelemetryLogs()
    val telegramConfig: Flow<TelegramBotConfig?> = dao.getTelegramConfig()

    suspend fun initializeDefaultDataIfEmpty() {
        if (dao.getChildProfile().firstOrNull() == null) {
            dao.saveChildProfile(ChildProfile())
        }

        if (dao.getTelegramConfig().firstOrNull() == null) {
            dao.saveTelegramConfig(TelegramBotConfig())
        }

        if (dao.getAllGeofences().firstOrNull().isNullOrEmpty()) {
            dao.insertGeofence(
                GeofenceZone(
                    name = "Home Zone",
                    latitude = 37.7749,
                    longitude = -122.4194,
                    radiusMeters = 200f,
                    category = "SAFE",
                    isInside = true
                )
            )
            dao.insertGeofence(
                GeofenceZone(
                    name = "Lincoln Middle School",
                    latitude = 37.7833,
                    longitude = -122.4167,
                    radiusMeters = 350f,
                    category = "SCHOOL",
                    isInside = false
                )
            )
        }

        if (dao.getAllAlerts().firstOrNull().isNullOrEmpty()) {
            dao.insertAlert(
                SafetyAlert(
                    title = "Geofence Departure",
                    description = "Leo departed Home Zone at 08:15 AM (Headings: School Route)",
                    type = "GEOFENCE_EXIT",
                    severity = AlertSeverity.INFO,
                    timestamp = System.currentTimeMillis() - 3600000 * 2
                )
            )
            dao.insertAlert(
                SafetyAlert(
                    title = "On-Device Safety Flagged Text",
                    description = "Detected cyberbullying keyword ('don't tell anyone') in chat messenger",
                    type = "CYBERBULLYING",
                    severity = AlertSeverity.WARNING,
                    timestamp = System.currentTimeMillis() - 1800000,
                    helplineResource = "Childhelp National Hotline: 1-800-422-4453"
                )
            )
        }

        if (dao.getTelemetryLogs().firstOrNull().isNullOrEmpty()) {
            dao.insertTelemetry(
                TelemetryData(
                    batteryLevel = 88,
                    isCharging = false,
                    isScreenOn = true,
                    wifiSsid = "School_Wifi_Secure",
                    locationName = "Lincoln High Campus",
                    currentApp = "Khan Academy Math"
                )
            )
        }
    }

    suspend fun saveChildProfile(profile: ChildProfile) = dao.saveChildProfile(profile)

    suspend fun saveTelegramConfig(config: TelegramBotConfig) = dao.saveTelegramConfig(config)

    suspend fun addAlert(alert: SafetyAlert) {
        dao.insertAlert(alert)
        // If telegram bot is configured, attempt sending alert
        val config = dao.getTelegramConfig().firstOrNull()
        if (config != null && config.isConnected && config.botToken.isNotEmpty() && config.chatId.isNotEmpty()) {
            val emoji = when (alert.severity) {
                AlertSeverity.CRITICAL -> "🚨"
                AlertSeverity.WARNING -> "⚠️"
                AlertSeverity.INFO -> "ℹ️"
            }
            val msg = "<b>$emoji DOUD PROTECTION ALERT: ${alert.title}</b>\n\n${alert.description}\n\n<i>Time:</i> ${java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(alert.timestamp)}"
            telegramService.sendMessage(config.botToken, config.chatId, msg)
        }
    }

    suspend fun resolveAlert(alertId: Long) = dao.resolveAlert(alertId)

    suspend fun addGeofence(geofence: GeofenceZone) = dao.insertGeofence(geofence)

    suspend fun deleteGeofence(id: Long) = dao.deleteGeofence(id)

    suspend fun addTelemetry(telemetry: TelemetryData) = dao.insertTelemetry(telemetry)

    suspend fun validateTelegramBotToken(token: String): Result<String> = telegramService.validateBotToken(token)

    suspend fun sendTelegramTestMessage(token: String, chatId: String, text: String): Result<Boolean> =
        telegramService.sendMessage(token, chatId, text)
}
