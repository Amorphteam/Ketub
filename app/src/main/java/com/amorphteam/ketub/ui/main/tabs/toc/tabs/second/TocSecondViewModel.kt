package com.amorphteam.ketub.ui.main.tabs.toc.tabs.second

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.toc.model.TocGroupItem
import com.amorphteam.ketub.utility.TempData

class TocSecondViewModel : ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()


    private val _tocGroupItems = MutableLiveData<List<TocGroupItem>>()
    val tocGroupItems: LiveData<List<TocGroupItem>> = _tocGroupItems

    init {
        _tocGroupItems.value = TempData.indexItems
    }

    fun openEpubAct() {
        startEpubAct.value = true
    }}