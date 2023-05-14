package com.amorphteam.ketub.utility

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

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


    fun saveStyleBookPref(styleBookPref: StyleBookPreferences?) {
        val styleString:String = Gson().toJson(styleBookPref, StyleBookPreferences::class.java)
        sharedPreferences.edit().putString(Keys.STYLE_BOOK_PREF, styleString).apply()

    }

    fun getStyleBookPref(): StyleBookPreferences {
        val styleString = sharedPreferences.getString(Keys.STYLE_BOOK_PREF, "")
        var styleBookPreferences:StyleBookPreferences? = Gson().fromJson(styleString, StyleBookPreferences::class.java)
        if (styleBookPreferences == null){
            styleBookPreferences = StyleBookPreferences()
        }
        return styleBookPreferences
    }

}
