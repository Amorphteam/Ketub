package com.amorphteam.ketub.ui.main.tabs.library

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.TempData

class LibraryFragmentViewModel: ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()
    var startSearchAct = MutableLiveData<Boolean>()
    var startDetailFrag = MutableLiveData<Boolean>()

    init {
        Log.i(Keys.LOG_NAME, "main view model created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(Keys.LOG_NAME, "main view model was cleared")
    }

    fun getEjtihadItem():MutableLiveData<ArrayList<BookModel>>{
        val array = MutableLiveData<ArrayList<BookModel>>()
        array.value = TempData.bookArray
        return array
    }

    fun getNososItem():MutableLiveData<ArrayList<BookModel>>{
        val array = MutableLiveData<ArrayList<BookModel>>()
        array.value = TempData.bookArray
        return array
    }


    fun getReadMoreMainToc():MutableLiveData<ArrayList<MainToc>>{
        val array = MutableLiveData<ArrayList<MainToc>>()
        array.value = TempData.mostRead
        return array
    }

    fun getRecommandedToc():MutableLiveData<ArrayList<MainToc>>{
        val array = MutableLiveData<ArrayList<MainToc>>()
        array.value = TempData.mostRead
        return array
    }
    fun openEpubAct(){
        startEpubAct.value = true
    }

    fun openDetailFrag(){
        startDetailFrag.value = true
    }
    fun openSearchAct(){
        startSearchAct.value = true
    }
}