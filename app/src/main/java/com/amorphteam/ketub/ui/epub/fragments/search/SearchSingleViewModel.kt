package com.amorphteam.ketub.ui.epub.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.search.model.SearchModel
import com.amorphteam.ketub.utility.TempData

class SearchSingleViewModel : ViewModel() {
    private val _startEpubFrag = MutableLiveData<Boolean>()
    val startEpubFrag: LiveData<Boolean>
        get() = _startEpubFrag


    fun getSearchList(): MutableLiveData<ArrayList<SearchModel>> {
        val array = MutableLiveData<ArrayList<SearchModel>>()
        array.value = TempData.searchResult
        return array
    }

    fun openEpubFrag() {
        _startEpubFrag.value = true
    }
}