package com.amorphteam.ketub.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.search.model.SearchModel
import com.amorphteam.ketub.utility.TempData

class SearchViewModel : ViewModel() {

    private val _startEpubAct = MutableLiveData<Boolean>()
    val startEpubAct: LiveData<Boolean>
        get() = _startEpubAct

    fun getSearchList(): MutableLiveData<ArrayList<SearchModel>> {
        val array = MutableLiveData<ArrayList<SearchModel>>()
        array.value = TempData.searchResult
        return array
    }


    fun openEpubAct() {
        _startEpubAct.value = true
    }
}