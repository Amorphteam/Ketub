package com.amorphteam.ketub.ui.main.tabs.library.database

import androidx.room.Dao
import androidx.room.Query
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel


@Dao
interface BookDatabaseDao {

    @Query("select * from Book")
    fun getAllBooks(): List<BookModel>

    @Query("select * from Book")
    fun getAllItemsForFirstCatBooks(): List<BookModel>

    @Query("select * from Book")
    fun getAllItemsForSecondCatBooks(): List<BookModel>

    @Query("select * from Book order by ID DESC limit 5")
    fun getNewItemsForFirstCatBooks(): List<BookModel>

    @Query("select * from Book order by ID DESC limit 5")
    fun getNewItemsForSecondCatBooks(): List<BookModel>


}