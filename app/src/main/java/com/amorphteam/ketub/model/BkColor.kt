package com.amorphteam.ketub.model

enum class BkColor (s:String){
    NONE(""),
    CREAM("cream"),
    BLACK("black"),
    GREY("grey");

    fun equalsName(otherName: String?): Boolean {
        return otherName != null && name == otherName
    }
}