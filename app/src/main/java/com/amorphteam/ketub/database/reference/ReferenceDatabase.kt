package com.amorphteam.ketub.database.reference

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amorphteam.ketub.model.ReferenceModel
import com.amorphteam.ketub.utility.Keys

@Database(entities = [ReferenceModel::class], version = 1, exportSchema = false)

abstract class ReferenceDatabase : RoomDatabase() {

    abstract val referenceDatabaseDao: ReferenceDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ReferenceDatabase? = null

        fun getInstance(context: Context): ReferenceDatabase {
            synchronized(this) {

                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ReferenceDatabase::class.java,
                        Keys.DB_REFERENCE_NAME
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