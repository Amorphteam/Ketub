package com.amorphteam.ketub.model

enum class FontSize(s: String) {

    SIZE1("size1"),
    SIZE2("size2"),
    SIZE3("size3"),
    SIZE4("size4"),
    SIZE5("size5");

    private val mValues: Array<FontSize> = values()

    fun equalsName(otherName: String?): Boolean {
        return otherName != null && name == otherName
    }

    fun next(): FontSize {
        if (this.ordinal + 1 >= mValues.size) {
            return mValues[mValues.size - 1]
        }
        return mValues[(this.ordinal + 1) % mValues.size]
    }

    fun prev(): FontSize {
        if (this.ordinal - 1 <= 0) {
            return mValues[0]
        }
        return mValues[(this.ordinal - 1) % mValues.size]
    }

}



