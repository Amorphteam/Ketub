package com.amorphteam.ketub.model

import android.util.Log
import java.lang.Exception

enum class FontSize(var number: Int) {
    SIZE0(0),
    SIZE1(1),
    SIZE2(2),
    SIZE3(3),
    SIZE4(4);

    companion object {
        fun from(findValue: Int): FontSize {
            return try {
                FontSize.values().first { it.number == findValue }
            }catch (e: Exception){
                SIZE0
            }
        }
    }
}



