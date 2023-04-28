package com.amorphteam.ketub.ui.main.tabs.bookmark.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel


@Dao
interface BookmarkDatabaseDao {

    @Query("select * from bookmark_list")
    fun getAllBookmarks(): List<BookmarkModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDefaultData(bookmark: List<BookmarkModel>)

}