package com.amorphteam.ketub.ui.main.tabs.library.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import kotlinx.coroutines.selects.select


@Dao
interface BookDatabaseDao {

    @Query("select * from Book")
    fun getAllBooks(): List<BookModel>

    @Query("select * from Book")
    fun getEjtehadBooks(): List<BookModel>

    @Query("select * from Book")
    fun getNososBooks(): List<BookModel>
}