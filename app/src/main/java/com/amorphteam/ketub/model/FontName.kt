package com.amorphteam.ketub.model

import java.lang.Exception

enum class FontName(var number: Int) {
    FONT0(0),
    FONT1(1),
    FONT2(2),
    FONT3(3),
    FONT4(4);
    companion object {
        fun from(findValue: Int): FontName {
            return try {
                FontName.values().first { it.number == findValue }
            }catch (e: Exception){
                FONT1
            }
        }
    }
}