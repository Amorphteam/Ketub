package com.amorphteam.ketub.utility

import com.google.gson.Gson

class PreferencesHelper {
    companion object{
        fun convertStyleBookToString(styleBookPreferences: StyleBookPreferences):String{
            return Gson().toJson(styleBookPreferences, StyleBookPreferences::class.java)
        }

        fun convertStringToStyleBook(string: String):StyleBookPreferences?{
            return Gson().fromJson(string, StyleBookPreferences::class.java)
        }
    }
}