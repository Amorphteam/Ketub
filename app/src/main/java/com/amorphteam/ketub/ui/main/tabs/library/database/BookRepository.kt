package com.amorphteam.ketub.ui.main.tabs.library.database

import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel

class BookRepository(private val bookDatabaseDao: BookDatabaseDao) {

    fun getAllBooks(): List<BookModel> = bookDatabaseDao.getAllBooks()

    fun getAllBooks(titleBook:String): List<BookModel> =
        bookDatabaseDao.getAllBooks(titleBook)


    fun getAllBooks(titleBook:String, count:Int): List<BookModel> =
        bookDatabaseDao.getAllBooks(titleBook, count)


}