package com.amorphteam.ketub.ui.main.tabs.library

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.utility.TempData

class DetailViewModel : ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()
    var startLibraryFrag = MutableLiveData<Boolean>()

    fun getEjtihadItem(): MutableLiveData<ArrayList<BookModel>> {
        val array = MutableLiveData<ArrayList<BookModel>>()
        array.value = TempData.bookArray
        return array
    }

    fun openEpubAct(){
        startEpubAct.value = true
    }

    fun openLibraryFrag(){
        startLibraryFrag.value = true
    }
}