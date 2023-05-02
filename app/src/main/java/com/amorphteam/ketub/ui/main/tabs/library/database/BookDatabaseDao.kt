package com.amorphteam.ketub.ui.main.tabs.library.database

import androidx.room.Dao
import androidx.room.Query
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel


@Dao
interface BookDatabaseDao {

    @Query("select * from category_list")
    fun getAllBooks(): List<BookModel>

    @Query("select * from category_list WHERE category_name LIKE '%' || :titleBook || '%' ORDER BY _id ASC")
    fun getAllBooks(titleBook:String): List<BookModel>

    @Query("select * from category_list WHERE category_name LIKE '%' || :titleBook || '%' ORDER BY _Id ASC limit :count ")
    fun getAllBooks(titleBook:String, count:Int): List<BookModel>


}