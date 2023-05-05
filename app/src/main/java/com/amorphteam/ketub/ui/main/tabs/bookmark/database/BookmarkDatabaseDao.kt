package com.amorphteam.ketub.ui.main.tabs.bookmark.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel


@Dao
interface BookmarkDatabaseDao {

    @Query("select * from bookmark_list")
    fun getAllBookmarks(): List<BookmarkModel>

    @Query("select * from bookmark_list  WHERE book_name LIKE '%' || :catName || '%'")
    fun getAllBookmarks(catName:String): List<BookmarkModel>


    @Query("select * from bookmark_list WHERE book_name = :bookName")
    fun getAllBookmarksForSingleBook(bookName:String): List<BookmarkModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: BookmarkModel)

    @Query("DELETE FROM bookmark_list WHERE id = :id")
    suspend fun delete(id: Int)

}