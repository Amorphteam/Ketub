package com.amorphteam.ketub.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.ui.main.tabs.library.LibraryViewModel

class SearchViewModelFactory  (private val bookRepository: BookRepository

) : ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(bookRepository) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}