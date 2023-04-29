package com.amorphteam.ketub.ui.main.tabs.index.tabLayout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexGroupItem
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexModel
import com.amorphteam.ketub.utility.TempData

class IndexListFirstViewModel : ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()

    private val _items = MutableLiveData<List<IndexGroupItem>>()
    val items: LiveData<List<IndexGroupItem>> = _items



    init {
        _items.value = TempData.indexItems
    }


    fun openEpubAct() {
        startEpubAct.value = true
    }
}