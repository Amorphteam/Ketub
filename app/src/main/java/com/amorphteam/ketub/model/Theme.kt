package com.amorphteam.ketub.model

import java.lang.Exception

enum class Theme(var number: Int) {
    BASE(0),
    LIGHT(1),
    DARK(2);

    companion object {
        fun from(findValue: Int): Theme {
            return try {
                Theme.values().first { it.number == findValue }
            }catch (e: Exception){
                BASE
            }
        }
    }
}