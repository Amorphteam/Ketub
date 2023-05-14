package com.amorphteam.ketub.model

import java.lang.Exception

enum class FontName(var number: Int) {
    FONT1(0),
    FONT2(1),
    FONT3(2),
    FONT4(3),
    FONT5(4);
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