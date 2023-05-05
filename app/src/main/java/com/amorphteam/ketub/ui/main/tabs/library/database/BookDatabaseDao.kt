package com.amorphteam.ketub.ui.main.tabs.library.database

import androidx.room.Dao
import androidx.room.Query
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel


@Dao
interface BookDatabaseDao {

    @Query("select * from book_list")
    fun getAllBooks(): List<BookModel>

    @Query("select * from book_list WHERE book_name LIKE '%' || :bookName || '%' ORDER BY _id ASC")
    fun getAllBooks(bookName:String): List<BookModel>

    @Query("select * from book_list WHERE book_name LIKE '%' || :bookName || '%' ORDER BY _Id ASC limit :count ")
    fun getAllBooks(bookName:String, count:Int): List<BookModel>


}