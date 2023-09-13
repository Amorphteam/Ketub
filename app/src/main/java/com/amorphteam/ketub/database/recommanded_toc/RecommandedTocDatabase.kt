package com.amorphteam.ketub.database.recommanded_toc

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amorphteam.ketub.model.RecommandedTocModel
import com.amorphteam.ketub.utility.Keys

@Database(entities = [RecommandedTocModel::class], version = 1, exportSchema = false)

abstract class RecommandedTocDatabase : RoomDatabase() {

    abstract val recommandedTocDatabaseDao: RecommandedTocDao

    companion object {
        @Volatile
        private var INSTANCE: RecommandedTocDatabase? = null

        fun getInstance(context: Context): RecommandedTocDatabase {
            synchronized(this) {

                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecommandedTocDatabase::class.java,
                        Keys.DB_RECOMMANDED_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .createFromAsset(Keys.DB_RECOMMANDED_ADDRESS)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}