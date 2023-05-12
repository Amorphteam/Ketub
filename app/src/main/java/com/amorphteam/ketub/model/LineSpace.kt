package com.amorphteam.ketub.model

enum class LineSpace(s: String) {

    SPACE1("space1"),
    SPACE2("space2"),
    SPACE3("space3"),
    SPACE4("space4"),
    SPACE5("space5");

    private val mValues: Array<LineSpace> = LineSpace.values()

    fun equalsName(otherName: String?): Boolean {
        return otherName != null && name == otherName
    }

    fun next(): LineSpace {
        if (this.ordinal + 1 >= mValues.size) {
            return mValues[mValues.size - 1]
        }
        return mValues[(this.ordinal + 1) % mValues.size]
    }

    fun prev(): LineSpace {
        if (this.ordinal - 1 <= 0) {
            return mValues[0]
        }
        return mValues[(this.ordinal - 1) % mValues.size]
    }

}