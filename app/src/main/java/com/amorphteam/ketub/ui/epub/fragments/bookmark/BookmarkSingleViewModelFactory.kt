package com.amorphteam.ketub.ui.epub.fragments.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.database.reference.ReferenceDatabaseDao

class BookmarkSingleViewModelFactory (
    private val dataSource: ReferenceDatabaseDao
) : ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkSingleViewModel::class.java)) {
            return BookmarkSingleViewModel(dataSource) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}