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

    private var _allBooks = MutableLiveData<List<BookModel>>()
    val allBooks: LiveData<List<BookModel>>
        get() = _allBooks

    private val repository: BookRepository = BookRepository(bookDatabaseDao)

    var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        initializeBook()
    }

    private fun initializeBook() {
        uiScope.launch {
            _allBooks.value = getAllBookFromDatabase()
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
