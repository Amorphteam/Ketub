package com.amorphteam.ketub.model

import java.lang.Exception

enum class QuickStyle(var number: Int) {
    DEFAULT(0),
    READABILITY(1),
    DARKMODE(2),
    HIGHCONTRAST(3);
    companion object {
        fun from(findValue: Int): QuickStyle {
            return try {
                QuickStyle.values().first { it.number == findValue }
            }catch (e: Exception){
                DEFAULT
            }
        }
    }
}