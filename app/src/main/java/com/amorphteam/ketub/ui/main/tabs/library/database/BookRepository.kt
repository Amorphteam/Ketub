package com.amorphteam.ketub.ui.main.tabs.library.database

import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel

class BookRepository(private val bookDatabaseDao: BookDatabaseDao) {

    fun getAllBooks(): List<BookModel> = bookDatabaseDao.getAllBooks()

    fun getAllBooks(catName:String): List<BookModel> =
        bookDatabaseDao.getAllBooks(catName)


    fun getAllBooks(catName:String, count:Int): List<BookModel> =
        bookDatabaseDao.getAllBooks(catName, count)


}