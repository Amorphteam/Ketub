package com.amorphteam.ketub.ui.main.tabs.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.database.reference.ReferenceRepository


open class LibraryViewModelFactory(
    private val bookRepository: BookRepository,
    private val referenceRepository: ReferenceRepository


) : ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            return LibraryViewModel(bookRepository, referenceRepository) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}