package com.amorphteam.ketub.ui.main.tabs.library.database

import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel

class BookRepository(private val bookDatabaseDao: BookDatabaseDao) {

    fun getAllBooks(): List<BookModel> = bookDatabaseDao.getAllBooks()

    fun getAllItemsForFirstCatBooks(): List<BookModel> =
        bookDatabaseDao.getAllItemsForFirstCatBooks()

    fun getAllItemsForSecondCatBooks(): List<BookModel> =
        bookDatabaseDao.getAllItemsForSecondCatBooks()

    fun getNewItemsForFirstCatBooks(): List<BookModel> =
        bookDatabaseDao.getNewItemsForFirstCatBooks()

    fun getNewItemsForSecondCatBooks(): List<BookModel> =
        bookDatabaseDao.getNewItemsForSecondCatBooks()

}