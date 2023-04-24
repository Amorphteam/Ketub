package com.amorphteam.ketub.ui.main.tabs.library

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabaseDao

class LibraryFragmentViewModelFactory(private val dataSource: BookDatabaseDao, private val application: Application): ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LibraryFragmentViewModel::class.java)){
            return LibraryFragmentViewModel(dataSource, application) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}