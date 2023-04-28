package com.amorphteam.ketub.ui.main.tabs.bookmark.database

import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel


class BookmarkRepository(private val bookmarkDatabaseDao: BookmarkDatabaseDao) {

    fun getAllBookmarks(): List<BookmarkModel> = bookmarkDatabaseDao.getAllBookmarks()

    suspend fun insertDefaultData(bookmark : List<BookmarkModel>) = bookmarkDatabaseDao.insertDefaultData(bookmark)
}