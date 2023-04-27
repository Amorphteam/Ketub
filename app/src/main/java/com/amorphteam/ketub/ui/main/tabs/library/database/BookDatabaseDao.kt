package com.amorphteam.ketub.ui.main.tabs.library.database

import androidx.room.Dao
import androidx.room.Query
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel


@Dao
interface BookDatabaseDao {

    @Query("select * from category_list")
    fun getAllBooks(): List<BookModel>

    @Query("select * from category_list")
    fun getAllItemsForFirstCatBooks(): List<BookModel>

    @Query("select * from category_list")
    fun getAllItemsForSecondCatBooks(): List<BookModel>

    @Query("select * from category_list order by _Id DESC limit 5")
    fun getNewItemsForFirstCatBooks(): List<BookModel>

    @Query("select * from category_list order by _Id DESC limit 5")
    fun getNewItemsForSecondCatBooks(): List<BookModel>


}