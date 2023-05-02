package com.amorphteam.ketub.ui.main.tabs.library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.*
import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabaseDao
import com.amorphteam.ketub.ui.main.tabs.library.database.BookRepository
import com.amorphteam.ketub.ui.main.tabs.library.api.TocApi
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc
import com.amorphteam.ketub.ui.main.tabs.library.model.TitleAndDes
import com.amorphteam.ketub.utility.Keys
import kotlinx.coroutines.*

class LibraryViewModel(private val bookDatabaseDao: BookDatabaseDao) : ViewModel() {
    private val _startEpubAct = MutableLiveData<Boolean>()
    val startEpubAct: LiveData<Boolean>
        get() = _startEpubAct

    private val _startSearchAct = MutableLiveData<Boolean>()
    val startSearchAct: LiveData<Boolean>
        get() = _startSearchAct

    private val _startDetailFrag = MutableLiveData<TitleAndDes>()
    val startDetailFrag: LiveData<TitleAndDes>
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

    private val repository: BookRepository = BookRepository(bookDatabaseDao)


    private var _firstCatBooksNewItems = MutableLiveData<List<BookModel>>()
    val firstCatBooksNewItems: LiveData<List<BookModel>>
        get() = _firstCatBooksNewItems

    private var _secondCatBooksNewItems = MutableLiveData<List<BookModel>>()
    val secondCatBooksNewItems: LiveData<List<BookModel>>
        get() = _secondCatBooksNewItems

    init {
        initializeBooks()
        getReadMoreMainToc()
        getRecommandedToc()
    }

    private fun initializeBooks() {
        uiScope.launch {
            _firstCatBooksNewItems.value = getAllBooks(Keys.DB_FIRST_CAT, Keys.DB_BOOK_LIMIT_COUNT)
            _secondCatBooksNewItems.value = getAllBooks(Keys.DB_SECOND_CAT, Keys.DB_BOOK_LIMIT_COUNT)
        }
    }


    private suspend fun getAllBooks(catName:String, count:Int): List<BookModel> {
        return withContext(Dispatchers.IO) {
            val book = repository.getAllBooks(catName,count)
            book
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i(Keys.LOG_NAME, "main view model was cleared")
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

    fun openDetailFrag(title:TitleAndDes) {
        _startDetailFrag.value = title
    }

    fun openSearchAct() {
        _startSearchAct.value = true
    }


}
