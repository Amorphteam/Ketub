package com.amorphteam.ketub.ui.main.tabs.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabaseDao
import com.amorphteam.ketub.ui.main.tabs.library.model.TitleAndDes

class DetailViewModelFactory (
    private val dataSource: BookDatabaseDao,
    private val titleAndDes: TitleAndDes
) : ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dataSource, titleAndDes) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}