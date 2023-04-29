package com.amorphteam.ketub.ui.main.tabs.index.tabLayout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.index.adapter.ExpandableAdapter
import com.amorphteam.ketub.ui.main.tabs.index.model.ChildItem
import com.amorphteam.ketub.ui.main.tabs.index.model.GroupItem
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexModel
import com.amorphteam.ketub.utility.TempData

class IndexListFirstViewModel : ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()

    private val _items = MutableLiveData<List<GroupItem>>()
    val items: LiveData<List<GroupItem>> = _items



    init {
        _items.value = TempData.indexItems
    }



    fun getIndexList(): MutableLiveData<ArrayList<IndexModel>> {
        val array = MutableLiveData<ArrayList<IndexModel>>()
        array.value = TempData.bookIndex
        return array
    }

    fun openEpubAct() {
        startEpubAct.value = true
    }
}