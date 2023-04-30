package com.amorphteam.ketub.ui.epub.fragments.toc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexGroupItem
import com.amorphteam.ketub.utility.TempData

class TocSingleViewModel : ViewModel() {
    var startEpubFrag = MutableLiveData<Boolean>()

    private val _indexGroupItems = MutableLiveData<List<IndexGroupItem>>()
    val indexGroupItems: LiveData<List<IndexGroupItem>> = _indexGroupItems

    init {
        _indexGroupItems.value = TempData.indexItems
    }

    fun openEpubFrag() {
        startEpubFrag.value = true
    }}