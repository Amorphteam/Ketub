package com.amorphteam.ketub.database.book

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amorphteam.ketub.model.BookModel
import com.amorphteam.ketub.model.CategoryModel
import com.amorphteam.ketub.utility.Keys


@Database(entities = [BookModel::class, CategoryModel::class], version = 1, exportSchema = false)

abstract class BookDatabase : RoomDatabase() {

    abstract val bookDatabaseDao: BookDatabaseDao

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
                        Keys.DB_BOOK_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .createFromAsset(Keys.DB_BOOK_ADDRESS)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}
