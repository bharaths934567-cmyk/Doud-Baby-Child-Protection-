package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "telegram_config")
data class TelegramBotConfig(
    @PrimaryKey val id: Int = 1,
    val botToken: String = "",
    val chatId: String = "",
    val isConnected: Boolean = false,
    val botUsername: String = "DoudChildMonitorBot",
    val hmacKey: String = "HMAC_SHA256_DOUD_KEY_2026",
    val autoSendAlerts: Boolean = true
)
