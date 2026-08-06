package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.*

@Database(
    entities = [
        ChildProfile::class,
        SafetyAlert::class,
        GeofenceZone::class,
        TelemetryData::class,
        TelegramBotConfig::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ProtectionDatabase : RoomDatabase() {

    abstract fun protectionDao(): ProtectionDao

    companion object {
        @Volatile
        private var INSTANCE: ProtectionDatabase? = null

        fun getDatabase(context: Context): ProtectionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProtectionDatabase::class.java,
                    "doud_protection_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
