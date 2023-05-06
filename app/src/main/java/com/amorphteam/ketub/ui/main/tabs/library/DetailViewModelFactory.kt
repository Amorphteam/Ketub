package com.amorphteam.ketub.ui.main.tabs.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.model.CatSection

class DetailViewModelFactory(
    private val bookRepository: BookRepository,
    private val catSection: CatSection
) : ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(bookRepository, catSection) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}