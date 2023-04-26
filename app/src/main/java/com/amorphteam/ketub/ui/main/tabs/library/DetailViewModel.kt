package com.amorphteam.ketub.ui.main.tabs.library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabaseDao
import com.amorphteam.ketub.ui.main.tabs.library.database.BookRepository
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.utility.Keys
import kotlinx.coroutines.*

class DetailViewModel(private val bookDatabaseDao: BookDatabaseDao) : ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()
    var startLibraryFrag = MutableLiveData<Boolean>()

    private val repository: BookRepository = BookRepository(bookDatabaseDao)

    var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _firstCatBooksAllItems = MutableLiveData<List<BookModel>>()
    val firstCatBooksAllItems: LiveData<List<BookModel>>
        get() = _firstCatBooksAllItems

    private var _secondCatBooksAllItems = MutableLiveData<List<BookModel>>()
    val secondCatBooksAllItems: LiveData<List<BookModel>>
        get() = _secondCatBooksAllItems

    init {
        initializeBooks()
    }

    private fun initializeBooks() {
        uiScope.launch {
            _firstCatBooksAllItems.value = getAllItemsForFirstCatBooksFromDatabase()
            _secondCatBooksAllItems.value = getAllItemsForSecondCatBooksFromDatabase()
            Log.i(Keys.LOG_NAME, "uiScope.launch")
        }
    }

    private suspend fun getAllItemsForFirstCatBooksFromDatabase(): List<BookModel>? {
        Log.i(Keys.LOG_NAME, "getAllItemsForFirstCatBooksFromDatabase")

        return withContext(Dispatchers.IO) {
            val book = repository.getAllItemsForFirstCatBooks()
            book
        }
    }

    private suspend fun getAllItemsForSecondCatBooksFromDatabase(): List<BookModel>? {
        Log.i(Keys.LOG_NAME, "getAllItemsForSecondCatBooksFromDatabase")

        return withContext(Dispatchers.IO) {
            val book = repository.getAllItemsForSecondCatBooks()
            book
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i(Keys.LOG_NAME, "main view model was cleared")
    }


    fun openEpubAct() {
        startEpubAct.value = true
    }

    fun openLibraryFrag() {
        startLibraryFrag.value = true
    }
}