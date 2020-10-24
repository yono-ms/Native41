package com.example.native41

import android.app.Application
import androidx.preference.PreferenceManager

class App : Application() {
    companion object {
        lateinit var prefs: AppPreference
    }

    override fun onCreate() {
        super.onCreate()
        prefs = AppPreference(PreferenceManager.getDefaultSharedPreferences(this), ::getString)
    }
}