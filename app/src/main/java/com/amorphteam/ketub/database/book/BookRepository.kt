package com.amorphteam.ketub.database.book

import com.amorphteam.ketub.model.BookModel
import com.amorphteam.ketub.model.CategoryModel

class BookRepository(private val bookDatabaseDao: BookDatabaseDao) {

    fun getAllCats(): List<CategoryModel> = bookDatabaseDao.getAllCats()

    fun getAllCats(catName:String): List<CategoryModel> =
        bookDatabaseDao.getAllCats(catName)


    fun getAllCats(catName:String, count:Int): List<CategoryModel> =
        bookDatabaseDao.getAllCats(catName, count)

    fun getAllBooks(catId: Int): List<BookModel> = bookDatabaseDao.getAllBooks(catId)
    fun getAllBooks(): List<BookModel> = bookDatabaseDao.getAllBooks()

}