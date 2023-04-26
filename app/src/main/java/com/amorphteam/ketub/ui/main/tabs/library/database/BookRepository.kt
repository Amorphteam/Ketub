package com.amorphteam.ketub.ui.main.tabs.library.database

import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabaseDao
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel

class BookRepository (private val bookDatabaseDao: BookDatabaseDao){
    fun getAllBooks(): List<BookModel> = bookDatabaseDao.getAllBooks()

}