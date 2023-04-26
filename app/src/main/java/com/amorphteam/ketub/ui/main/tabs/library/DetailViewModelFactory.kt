package com.amorphteam.ketub.ui.main.tabs.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabaseDao

class DetailViewModelFactory (
    private val dataSource: BookDatabaseDao
) : ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dataSource) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}