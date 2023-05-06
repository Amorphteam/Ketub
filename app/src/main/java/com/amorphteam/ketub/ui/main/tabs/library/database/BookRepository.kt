package com.amorphteam.ketub.ui.main.tabs.library.database

import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.main.tabs.library.model.CategoryModel

class BookRepository(private val bookDatabaseDao: BookDatabaseDao) {

    fun getAllCats(): List<CategoryModel> = bookDatabaseDao.getAllCats()

    fun getAllCats(catName:String): List<CategoryModel> =
        bookDatabaseDao.getAllCats(catName)


    fun getAllCats(catName:String, count:Int): List<CategoryModel> =
        bookDatabaseDao.getAllCats(catName, count)

    fun getAllBooks(catId: Int): List<BookModel> = bookDatabaseDao.getAllBooks(catId)

}