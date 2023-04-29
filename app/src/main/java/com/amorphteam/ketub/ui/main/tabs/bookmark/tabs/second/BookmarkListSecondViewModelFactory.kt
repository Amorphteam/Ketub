package com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkDatabaseDao

class BookmarkListSecondViewModelFactory(
    private val dataSource: BookmarkDatabaseDao
) : ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkListSecondViewModel::class.java)) {
            return BookmarkListSecondViewModel(dataSource) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}