package com.amorphteam.ketub.ui.main.tabs.index.tabLayout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexGroupItem
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexModel
import com.amorphteam.ketub.utility.TempData

class IndexListSecondViewModel : ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()


    private val _indexGroupItems = MutableLiveData<List<IndexGroupItem>>()
    val indexGroupItems: LiveData<List<IndexGroupItem>> = _indexGroupItems

    init {
        _indexGroupItems.value = TempData.indexItems
    }

    fun openEpubAct() {
        startEpubAct.value = true
    }}