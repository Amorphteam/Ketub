package com.amorphteam.ketub.ui.epub.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.utility.TempData

class SearchSingleViewModel : ViewModel() {
    private val _startEpubFrag = MutableLiveData<Boolean>()
    val startEpubFrag: LiveData<Boolean>
        get() = _startEpubFrag



    fun openEpubFrag() {
        _startEpubFrag.value = true
    }
}