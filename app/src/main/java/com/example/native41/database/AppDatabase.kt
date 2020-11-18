package com.example.native41.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CalModel::class, CalPageModel::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun calModelDao(): CalModelDao
    abstract fun calPageModelDao(): CalPageModelDao
}