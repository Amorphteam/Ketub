package com.amorphteam.ketub.utility

import android.app.Activity
import android.os.Build
import java.io.Serializable
import java.lang.Exception

class Keys {

    companion object {
        var LOG_NAME = "AJC KETUB"
        var URL_BASE_ADDRESS = "http://nosos.net/api/"
        val DB_BOOK_ADDRESS = "database/book_list.db"
        val DB_BOOK_NAME = "book_list.db"
        val DB_REFERENCE_NAME = "reference_database.db"
        val DB_FIRST_CAT = "الاجتهاد والتجديد"
        val DB_SECOND_CAT = "نصوص معاصرة"
        val DB_BOOK_LIMIT_COUNT = 5
        val NAV_CAT_SECTION = "catSection"


    }
}


enum class OnlineReference {
    NONE,
    READMORE_ONLINE,
    RECOMMENDED_ONLINE

}