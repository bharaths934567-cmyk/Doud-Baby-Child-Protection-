package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AlertSeverity { INFO, WARNING, CRITICAL }

@Entity(tableName = "safety_alerts")
data class SafetyAlert(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val type: String,
    val severity: AlertSeverity,
    val timestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false,
    val helplineResource: String? = null
)
