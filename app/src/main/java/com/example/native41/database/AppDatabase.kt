package com.example.native41.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CommitEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun commitEntityDao(): CommitEntityDao
}