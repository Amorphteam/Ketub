package com.amorphteam.ketub.utility

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Keys.PREFERENCES_NAME, Context.MODE_PRIVATE)


    fun loadAppVersion(): Int {
        return sharedPreferences.getInt(Keys.PREF_CURRENT_VERSION, Keys.PREF_DEFFAULT_APP_VERSION)
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
