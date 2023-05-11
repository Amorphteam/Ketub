package com.amorphteam.ketub.utility

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences =
            context.getSharedPreferences(Keys.PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun loadAppVersion(): Int {
        return sharedPreferences.getInt(Keys.PREF_CURRENT_VERSION, 0)
    }

    fun saveAppVersion(appVersion: Int) {
        sharedPreferences.edit().putInt(Keys.PREF_CURRENT_VERSION, appVersion).apply()
    }

    fun loadFirstRun(): Boolean {
        return sharedPreferences.getBoolean(Keys.PREF_FIRST_RUN, true)
    }

    fun saveFirstRun(firstRun: Boolean) {
        sharedPreferences.edit().putBoolean(Keys.PREF_FIRST_RUN, firstRun).apply()
    }

}
