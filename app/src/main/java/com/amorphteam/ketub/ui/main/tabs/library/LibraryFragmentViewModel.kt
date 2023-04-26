package com.amorphteam.ketub.ui.main.tabs.library

import android.util.Log
import androidx.lifecycle.*
import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabaseDao
import com.amorphteam.ketub.ui.main.tabs.library.database.BookRepository
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.TempData
import kotlinx.coroutines.*

class LibraryFragmentViewModel(private val bookDatabaseDao: BookDatabaseDao) : ViewModel() {

    var startEpubAct = MutableLiveData<Boolean>()
    var startSearchAct = MutableLiveData<Boolean>()
    var startDetailFrag = MutableLiveData<Boolean>()

    private val repository: BookRepository = BookRepository(bookDatabaseDao)

    var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _allBooks = MutableLiveData<List<BookModel>>()
    val allBooks: LiveData<List<BookModel>>
        get() = _allBooks

    private var _nososBooks = MutableLiveData<List<BookModel>>()
    val nososBooks: LiveData<List<BookModel>>
        get() = _nososBooks

    private var _ejtehadBooks = MutableLiveData<List<BookModel>>()
    val ejtehadBooks: LiveData<List<BookModel>>
        get() = _ejtehadBooks


    init {
        initializeBooks()
    }

    private fun initializeBooks() {
        uiScope.launch {
            _allBooks.value = getAllBookFromDatabase()
            _ejtehadBooks.value = getEjtehadBookFromDatabase()
            _nososBooks.value = getNososBookFromDatabase()
            Log.i(Keys.LOG_NAME, "uiScope.launch")
        }
    }

    private suspend fun getAllBookFromDatabase(): List<BookModel>? {
        Log.i(Keys.LOG_NAME, "getAllBookFromDatabase")

        return withContext(Dispatchers.IO) {
            val book = repository.getAllBooks()
            book
        }
    }

    private suspend fun getNososBookFromDatabase(): List<BookModel>? {
        Log.i(Keys.LOG_NAME, "getNososBookFromDatabase")

        return withContext(Dispatchers.IO) {
            val book = repository.getNososBooks()
            book
        }
    }

    private suspend fun getEjtehadBookFromDatabase(): List<BookModel>? {
        Log.i(Keys.LOG_NAME, "getEjtehadBookFromDatabase")

        return withContext(Dispatchers.IO) {
            val book = repository.getEjtehadBooks()
            book
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i(Keys.LOG_NAME, "main view model was cleared")
    }


    fun getReadMoreMainToc(): MutableLiveData<ArrayList<MainToc>> {
        val array = MutableLiveData<ArrayList<MainToc>>()
        array.value = TempData.mostRead
        return array
    }

    fun getRecommandedToc(): MutableLiveData<ArrayList<MainToc>> {
        val array = MutableLiveData<ArrayList<MainToc>>()
        array.value = TempData.mostRead
        return array
    }

    fun openEpubAct() {
        startEpubAct.value = true
    }

    fun openDetailFrag() {
        startDetailFrag.value = true
    }

    fun openSearchAct() {
        startSearchAct.value = true
    }

}
