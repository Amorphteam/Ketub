package com.amorphteam.ketub.ui.main.tabs.library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabaseDao
import com.amorphteam.ketub.ui.main.tabs.library.database.BookRepository
import com.amorphteam.ketub.ui.main.tabs.library.model.CategoryModel
import com.amorphteam.ketub.ui.main.tabs.library.model.TitleAndDes
import com.amorphteam.ketub.utility.Keys
import kotlinx.coroutines.*

class DetailViewModel(private val bookDatabaseDao: BookDatabaseDao, val titleAndDes:TitleAndDes) : ViewModel() {
    var startEpubAct = MutableLiveData<Boolean>()
    var startLibraryFrag = MutableLiveData<Boolean>()


    private val repository: BookRepository = BookRepository(bookDatabaseDao)

    var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private var _books = MutableLiveData<List<CategoryModel>>()
    val books: LiveData<List<CategoryModel>>
        get() = _books

    init {
        initializeBooks()
    }

    private fun initializeBooks() {
        uiScope.launch {
            _books.value = getAllBooks(titleAndDes.title)
            Log.i(Keys.LOG_NAME, "uiScope.launch")
        }
    }

    private suspend fun getAllBooks(catName:String): List<CategoryModel> {
        return withContext(Dispatchers.IO) {
            val book = repository.getAllBooks(catName)
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