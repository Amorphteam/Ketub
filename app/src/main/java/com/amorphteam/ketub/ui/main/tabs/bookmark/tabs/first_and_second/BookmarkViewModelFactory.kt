package com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.database.reference.ReferenceDatabaseDao

class BookmarkViewModelFactory (
    private val dataSource: ReferenceDatabaseDao,
    private val catName:String
) : ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            return BookmarkViewModel(dataSource, catName) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}