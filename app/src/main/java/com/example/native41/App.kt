package com.example.native41

import android.app.Application
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.example.native41.database.AppDatabase

class App : Application() {
    companion object {
        lateinit var prefs: AppPreference
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        prefs = AppPreference(PreferenceManager.getDefaultSharedPreferences(this), ::getString)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database").build()
    }
}