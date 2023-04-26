package com.amorphteam.ketub.ui.main.tabs.library

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.library.api.TocApi
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.TempData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LibraryFragmentViewModel: ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()
    var startSearchAct = MutableLiveData<Boolean>()
    var startDetailFrag = MutableLiveData<Boolean>()
    var readMoreToc = MutableLiveData<List<MainToc>>()
    var recommendedToc = MutableLiveData<List<MainToc>>()
    var errorRecommendedToc = MutableLiveData<String>()
    var errorMoreToc = MutableLiveData<String>()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope( Dispatchers.Main + viewModelJob)
    init {
        Log.i(Keys.LOG_NAME, "main view model created")
        getReadMoreMainToc()
        getRecommandedToc()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(Keys.LOG_NAME, "main view model was cleared")
        viewModelJob.cancel()
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


    fun getReadMoreMainToc(){
        uiScope.launch {
            val getReadMoreDefferedList = TocApi.retrofitService.getMostReadToc()
            try {
                val listResult = getReadMoreDefferedList.await()
                readMoreToc.value = listResult
            }catch (e:java.lang.Exception){
                errorMoreToc.value = "Failure: ${e.message}"

            }
        }

    }

    fun getRecommandedToc(){
        uiScope.launch {
            val getRecommandedDefferedList = TocApi.retrofitService.getRecommandedToc()
            try {
                val listResult = getRecommandedDefferedList.await()
                recommendedToc.value = listResult
            }catch (e:java.lang.Exception){
                errorRecommendedToc.value = "Failure: ${e.message}"

            }
        }
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