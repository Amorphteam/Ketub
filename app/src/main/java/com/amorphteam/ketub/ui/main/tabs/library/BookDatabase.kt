package com.amorphteam.ketub.ui.main.tabs.library

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel

@Database(entities = [BookModel::class], version = 1, exportSchema = false)

abstract class BookDatabase: RoomDatabase() {

    abstract fun getBookDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null
        private const val DB_NAME = "book_list.db"

        fun getDatabase(
            context: Context
        ): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    DB_NAME
                )
                    .createFromAsset("database/book_list.db")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}