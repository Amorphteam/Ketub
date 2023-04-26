package com.amorphteam.ketub.ui.main.tabs.library

import android.util.Log
import androidx.lifecycle.LiveData
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

class LibraryFragmentViewModel : ViewModel() {
    private val _startEpubAct = MutableLiveData<Boolean>()
    val startEpubAct: LiveData<Boolean>
        get() = _startEpubAct

    private val _startSearchAct = MutableLiveData<Boolean>()
    val startSearchAct: LiveData<Boolean>
        get() = _startSearchAct

    private val _startDetailFrag = MutableLiveData<Boolean>()
    val startDetailFrag: LiveData<Boolean>
        get() = _startDetailFrag

    private val _readMoreToc = MutableLiveData<List<MainToc>>()
    val readMoreToc: LiveData<List<MainToc>>
        get() = _readMoreToc

    private val _recommendedToc = MutableLiveData<List<MainToc>>()
    val recommendedToc: LiveData<List<MainToc>>
        get() = _recommendedToc

    private val _errorTocRecieve = MutableLiveData<String>()
    val errorTocRecieve: LiveData<String>
        get() = _errorTocRecieve

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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

    fun getEjtihadItem(): MutableLiveData<ArrayList<BookModel>> {
        val array = MutableLiveData<ArrayList<BookModel>>()
        array.value = TempData.bookArray
        return array
    }

    fun getNososItem(): MutableLiveData<ArrayList<BookModel>> {
        val array = MutableLiveData<ArrayList<BookModel>>()
        array.value = TempData.bookArray
        return array
    }


    fun getReadMoreMainToc() {
        uiScope.launch {
            val getReadMoreDefferedList = TocApi.retrofitService.getMostReadToc()
            try {
                val listResult = getReadMoreDefferedList.await()
                _readMoreToc.value = listResult
            } catch (e: java.lang.Exception) {
                _errorTocRecieve.value = "Failure: ${e.message}"

            }
        }

    }

    fun getRecommandedToc() {
        uiScope.launch {
            val getRecommandedDefferedList = TocApi.retrofitService.getRecommandedToc()
            try {
                val listResult = getRecommandedDefferedList.await()
                _recommendedToc.value = listResult
            } catch (e: java.lang.Exception) {
                _errorTocRecieve.value = "Failure: ${e.message}"

            }
        }
    }

    fun openEpubAct() {
        _startEpubAct.value = true
    }

    fun openDetailFrag() {
        _startDetailFrag.value = true
    }

    fun openSearchAct() {
        _startSearchAct.value = true
    }


}