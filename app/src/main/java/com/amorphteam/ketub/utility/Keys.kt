package com.amorphteam.ketub.utility

class Keys {

    companion object {
        val SPINE = "spine"
        var LOG_NAME = "AJC KETUB"
        var URL_BASE_ADDRESS = "http://nosos.net/api/"
        val DB_BOOK_ADDRESS = "database/book_list.db"
        val DB_BOOK_NAME = "book_list.db"
        val DB_REFERENCE_NAME = "reference_database.db"
        val DB_FIRST_CAT = "الاجتهاد والتجديد"
        val DB_SECOND_CAT = "نصوص معاصرة"
        val DB_BOOK_LIMIT_COUNT = 5
        val NAV_CAT_SECTION = "catSection"
        val MANIFEST_ITEM = "manifest_item"
        val POSITION_ITEM = "position_item"
        val MAX_SIDE_PAGE = 1
        val PREFERENCES_NAME = "sam_pref"
        val PREF_CURRENT_VERSION = "current_version"
        val PREF_FIRST_RUN = "first_run"
        val ASSET_BOOK_DIR = "books"
        val ASSET_COVER_DIR = "covers"
        val PREF_DEFFAULT_APP_VERSION = 0
        val BOOK_ADDRESS = "book_address"
            val NAV_POINT = "nav_point"
    }
}


enum class OnlineReference {
    NONE,
    READMORE_ONLINE,
    RECOMMENDED_ONLINE

}