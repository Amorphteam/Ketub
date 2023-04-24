package com.amorphteam.ketub.ui.main.tabs.library.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel


@Database(entities = [BookModel::class], version = 1, exportSchema = false)

abstract class BookDatabase : RoomDatabase() {

    abstract val sleepDatabaseDao: BookDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getInstance(context: Context): BookDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookDatabase::class.java,
                        "database/book_list"
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
