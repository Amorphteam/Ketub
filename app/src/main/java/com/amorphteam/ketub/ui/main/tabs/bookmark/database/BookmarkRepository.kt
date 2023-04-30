package com.amorphteam.ketub.ui.main.tabs.bookmark.database

import androidx.lifecycle.LiveData
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel


class BookmarkRepository(private val bookmarkDatabaseDao: BookmarkDatabaseDao) {

    fun getAllBookmarks(): List<BookmarkModel> = bookmarkDatabaseDao.getAllBookmarks()
    fun getAllBookmarksForSingleBook(bookName:String): List<BookmarkModel> = bookmarkDatabaseDao.getAllBookmarksForSingleBook(bookName)

    suspend fun insert(bookmark: BookmarkModel) =
        bookmarkDatabaseDao.insert(bookmark)

    suspend fun delete(id: Int) = bookmarkDatabaseDao.delete(id)


}