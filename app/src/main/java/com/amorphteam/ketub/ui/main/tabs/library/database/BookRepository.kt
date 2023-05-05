package com.amorphteam.ketub.ui.main.tabs.library.database

import com.amorphteam.ketub.ui.main.tabs.library.model.CategoryModel

class BookRepository(private val bookDatabaseDao: BookDatabaseDao) {

    fun getAllBooks(): List<CategoryModel> = bookDatabaseDao.getAllBooks()

    fun getAllBooks(catName:String): List<CategoryModel> =
        bookDatabaseDao.getAllBooks(catName)


    fun getAllBooks(catName:String, count:Int): List<CategoryModel> =
        bookDatabaseDao.getAllBooks(catName, count)


}