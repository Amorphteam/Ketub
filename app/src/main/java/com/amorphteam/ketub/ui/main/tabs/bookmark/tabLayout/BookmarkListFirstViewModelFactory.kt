package com.amorphteam.ketub.ui.main.tabs.bookmark.tabLayout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkDatabaseDao

class BookmarkListFirstViewModelFactory (
    private val dataSource: BookmarkDatabaseDao
) : ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkListFirstViewModel::class.java)) {
            return BookmarkListFirstViewModel(dataSource) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}