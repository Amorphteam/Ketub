package com.amorphteam.ketub.model

import android.util.Log
import java.lang.Exception

enum class LineSpace(var number: Int) {
    SPACE0(0),
    SPACE1(1),
    SPACE2(2),
    SPACE3(3),
    SPACE4(4);

    companion object {
        fun from(findValue: Int): LineSpace {
            return try {
                values().first { it.number == findValue }
            }catch (e: Exception){
                SPACE0
            }
        }
    }
}