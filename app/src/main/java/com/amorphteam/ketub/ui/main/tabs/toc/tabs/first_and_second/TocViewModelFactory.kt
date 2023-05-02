package com.amorphteam.ketub.ui.main.tabs.toc.tabs.first_and_second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TocViewModelFactory (
    private val catName:String
) : ViewModelProvider.Factory {

    @Suppress("unckecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TocViewModel::class.java)) {
            return TocViewModel(catName) as T
        }
        throw IllegalThreadStateException("Unknow ViewModel class")
    }
}