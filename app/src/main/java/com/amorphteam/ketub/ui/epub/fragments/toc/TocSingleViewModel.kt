package com.amorphteam.ketub.ui.epub.fragments.toc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.model.TocGroupItem
import com.amorphteam.ketub.utility.TempData

class TocSingleViewModel : ViewModel() {
    var startEpubFrag = MutableLiveData<Boolean>()

    private val _tocGroupItems = MutableLiveData<List<TocGroupItem>>()
    val tocGroupItems: LiveData<List<TocGroupItem>> = _tocGroupItems

    init {
        _tocGroupItems.value = TempData.indexItems
    }

    fun openEpubFrag() {
        startEpubFrag.value = true
    }}