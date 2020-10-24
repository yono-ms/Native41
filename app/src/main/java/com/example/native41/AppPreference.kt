package com.example.native41

import android.content.SharedPreferences

class AppPreference(private var prefs: SharedPreferences, val getKey: ((resId: Int) -> String)) {

    var login: String?
        get() {
            return prefs.getString(getKey(R.string.preference_login), null)
        }
        set(value) {
            prefs.edit().apply {
                if (value == null) {
                    remove(getKey(R.string.preference_login))
                } else {
                    putString(getKey(R.string.preference_login), value)
                }
                apply()
            }
        }
}