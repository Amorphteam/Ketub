package com.amorphteam.ketub.ui.main.tabs.bookmark.tabLayout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.utility.TempData

class BookmarkListSecondViewModel : ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()

    fun getIndexList(): MutableLiveData<ArrayList<BookmarkModel>> {
        val array = MutableLiveData<ArrayList<BookmarkModel>>()
        array.value = TempData.bookMarkArray
        return array
    }

    fun openEpubAct() {
        startEpubAct.value = true
    }
}