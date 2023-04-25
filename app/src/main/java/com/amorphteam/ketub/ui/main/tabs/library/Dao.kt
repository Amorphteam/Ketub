package com.amorphteam.ketub.ui.main.tabs.library

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel

@Dao
interface Dao {
    @Query("Select * from book")
    fun getAllBooks(): List<BookModel>
}
