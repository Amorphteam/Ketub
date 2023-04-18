package com.amorphteam.ketub.ui.main.tabs.index.tabLayout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexModel
import com.amorphteam.ketub.utility.TempData

class IndexListFirstViewModel : ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()

    fun getIndexList(): MutableLiveData<ArrayList<IndexModel>> {
        val array = MutableLiveData<ArrayList<IndexModel>>()
        array.value = TempData.bookIndex
        return array
    }

    fun openEpubAct() {
        startEpubAct.value = true
    }
}