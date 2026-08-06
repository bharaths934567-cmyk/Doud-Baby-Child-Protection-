package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_profiles")
data class ChildProfile(
    @PrimaryKey val id: String = "child_01",
    val name: String = "Leo",
    val ageGroup: String = "10-12 years (Under Supervision)",
    val platform: String = "Android 14 (Full Monitoring)",
    val isPaired: Boolean = true,
    val isSupervised: Boolean = true,
    val pairingToken: String = "DOUD-8849-SECURE",
    val hmacSignature: String = "a8f3e91b82c72191a82f3",
    val lastSyncTimestamp: Long = System.currentTimeMillis()
)
