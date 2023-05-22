package com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.database.reference.ReferenceDatabaseDao
import com.amorphteam.ketub.database.reference.ReferenceRepository

class BookmarkViewModelFactory (
    private val referenceRepository: ReferenceRepository,
    private val catName:String,
    private val signleBookName:String
) : ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            return BookmarkViewModel(referenceRepository, catName, signleBookName) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}