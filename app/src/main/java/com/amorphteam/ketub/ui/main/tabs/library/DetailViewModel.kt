package com.amorphteam.ketub.ui.main.tabs.library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabaseDao
import com.amorphteam.ketub.ui.main.tabs.library.database.BookRepository
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.TempData
import kotlinx.coroutines.*

class DetailViewModel(private val bookDatabaseDao: BookDatabaseDao) : ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()
    var startLibraryFrag = MutableLiveData<Boolean>()

    private val repository: BookRepository = BookRepository(bookDatabaseDao)

    var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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
            _ejtehadBooks.value = getEjtehadBookFromDatabase()
            _nososBooks.value = getNososBookFromDatabase()
            Log.i(Keys.LOG_NAME, "uiScope.launch")
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


    fun openEpubAct(){
        startEpubAct.value = true
    }

    fun openLibraryFrag(){
        startLibraryFrag.value = true
    }
}