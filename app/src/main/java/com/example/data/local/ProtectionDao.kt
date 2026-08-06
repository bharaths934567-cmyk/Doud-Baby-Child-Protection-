package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProtectionDao {

    // Child Profile
    @Query("SELECT * FROM child_profiles WHERE id = 'child_01'")
    fun getChildProfile(): Flow<ChildProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChildProfile(profile: ChildProfile)

    // Safety Alerts
    @Query("SELECT * FROM safety_alerts ORDER BY timestamp DESC")
    fun getAllAlerts(): Flow<List<SafetyAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: SafetyAlert)

    @Query("UPDATE safety_alerts SET isResolved = 1 WHERE id = :alertId")
    suspend fun resolveAlert(alertId: Long)

    @Query("DELETE FROM safety_alerts")
    suspend fun clearAlerts()

    // Geofences
    @Query("SELECT * FROM geofence_zones")
    fun getAllGeofences(): Flow<List<GeofenceZone>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeofence(geofence: GeofenceZone)

    @Query("DELETE FROM geofence_zones WHERE id = :id")
    suspend fun deleteGeofence(id: Long)

    // Telemetry Logs
    @Query("SELECT * FROM telemetry_logs ORDER BY timestamp DESC LIMIT 30")
    fun getTelemetryLogs(): Flow<List<TelemetryData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTelemetry(data: TelemetryData)

    // Telegram Bot Config
    @Query("SELECT * FROM telegram_config WHERE id = 1")
    fun getTelegramConfig(): Flow<TelegramBotConfig?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTelegramConfig(config: TelegramBotConfig)
}
