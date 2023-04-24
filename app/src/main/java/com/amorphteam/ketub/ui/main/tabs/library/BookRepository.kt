package com.amorphteam.ketub.ui.main.tabs.library

import androidx.lifecycle.LiveData
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel

class BookRepository(private val bookDao: Dao) {

    fun getAllBooks(): LiveData<List<BookModel>> = bookDao.getAllBooks()


}