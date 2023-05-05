package com.amorphteam.ketub.ui.main.tabs.bookmark.database

import androidx.lifecycle.LiveData
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel


class BookmarkRepository(private val bookmarkDatabaseDao: BookmarkDatabaseDao) {

    fun getAllBookmarks(): List<BookmarkModel> = bookmarkDatabaseDao.getAllBookmarks()

    fun getAllBookmarks(catName:String): List<BookmarkModel> = bookmarkDatabaseDao.getAllBookmarks(catName)

    fun getAllBookmarksForSingleBook(bookName:String): List<BookmarkModel> = bookmarkDatabaseDao.getAllBookmarksForSingleBook(bookName)

    suspend fun insert(bookmark: BookmarkModel) =
        bookmarkDatabaseDao.insert(bookmark)

    suspend fun delete(id: Int) = bookmarkDatabaseDao.delete(id)


}