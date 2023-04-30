package com.amorphteam.ketub.ui.epub.fragments.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkDatabaseDao

class BookmarkSingleViewModelFactory (
    private val dataSource: BookmarkDatabaseDao
) : ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkSingleViewModel::class.java)) {
            return BookmarkSingleViewModel(dataSource) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}