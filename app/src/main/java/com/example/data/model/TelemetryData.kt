package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "telemetry_logs")
data class TelemetryData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val batteryLevel: Int = 84,
    val isCharging: Boolean = false,
    val isScreenOn: Boolean = true,
    val wifiSsid: String = "Home_5G_Protected",
    val locationName: String = "Oak Avenue, Block 4",
    val currentApp: String = "Educational Quiz Reader",
    val encryptedPayload: String = "AES256GCM:9f8a3c...310d"
)
