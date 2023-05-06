package com.amorphteam.ketub.ui.main.tabs.library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.*
import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabaseDao
import com.amorphteam.ketub.ui.main.tabs.library.database.BookRepository
import com.amorphteam.ketub.ui.main.tabs.library.api.TocApi
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.main.tabs.library.model.CategoryModel
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


    private var _firstCatBooksNewItems = MutableLiveData<List<CategoryModel>>()
    val firstCatBooksNewItems: LiveData<List<CategoryModel>>
        get() = _firstCatBooksNewItems

    private var _secondCatBooksNewItems = MutableLiveData<List<CategoryModel>>()
    val secondCatBooksNewItems: LiveData<List<CategoryModel>>
        get() = _secondCatBooksNewItems

    private var _catBookItems = MutableLiveData<List<BookModel>>()
    val catBookItems: LiveData<List<BookModel>>
        get() = _catBookItems



    init {
        initializeBooks()
        getReadMoreMainToc()
        getRecommandedToc()
    }

    private fun initializeBooks() {
        uiScope.launch {
            _firstCatBooksNewItems.value = getAllCats(Keys.DB_FIRST_CAT, Keys.DB_BOOK_LIMIT_COUNT)
            _secondCatBooksNewItems.value = getAllCats(Keys.DB_SECOND_CAT, Keys.DB_BOOK_LIMIT_COUNT)
        }
    }


    private suspend fun getAllCats(catName:String, count:Int): List<CategoryModel> {
        return withContext(Dispatchers.IO) {
            val book = repository.getAllCats(catName,count)
            book
        }
    }

     private suspend fun getAllBooks(id: Int): List<BookModel> {
        return withContext(Dispatchers.IO) {
            val book = repository.getAllBooks(id)
            book
        }

     }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
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

     fun getCatId(id: Int) {
         uiScope.launch {
             _catBookItems.value = getAllBooks(id)
         }
    }
}
