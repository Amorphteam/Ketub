package com.amorphteam.ketub.ui.main.tabs.bookmark.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.utility.Keys

@Database(entities = [BookmarkModel::class], version = 1, exportSchema = false)

abstract class BookmarkDatabase : RoomDatabase() {

    abstract val bookmarkDatabaseDao: BookmarkDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: BookmarkDatabase? = null

        fun getInstance(context: Context): BookmarkDatabase {
            synchronized(this) {

                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookmarkDatabase::class.java,
                        Keys.DB_BOOKMARK_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}