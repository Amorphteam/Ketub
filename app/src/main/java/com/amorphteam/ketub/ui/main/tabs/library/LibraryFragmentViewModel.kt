package com.amorphteam.ketub.ui.main.tabs.library

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabase
import com.amorphteam.ketub.ui.main.tabs.library.database.BookRepository
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.TempData
import kotlinx.coroutines.*


class LibraryFragmentViewModel(application: Application) : AndroidViewModel(application) {
    var startEpubAct = MutableLiveData<Boolean>()
    var startSearchAct = MutableLiveData<Boolean>()
    var startDetailFrag = MutableLiveData<Boolean>()

    private val repository: BookRepository
    private val _allBooks = MutableLiveData<List<BookModel>>()
    val allBooks: LiveData<List<BookModel>>
        get() = _allBooks

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    init {
        Log.i(Keys.LOG_NAME, "main view model created")
        val dao = BookDatabase.getDatabase(application).getBookDao()
        repository = BookRepository(dao)
//            allBooks.value = repository.getAllBooks()
        initializeBook()

    }

    private fun initializeBook() {
        uiScope.launch {
            _allBooks.value = getBookFromDatabase()
            Log.i(Keys.LOG_NAME, "initializeBook")

        }
    }

    private suspend fun getBookFromDatabase(): List<BookModel>? {
        return withContext(Dispatchers.IO) {
            val book = repository.getAllBooks()
            book.value
        }
    }

    fun ejtihadItem(): MutableLiveData<List<BookModel>> {
        val array = MutableLiveData<List<BookModel>>()

        uiScope.launch {
            array.value = getBookFromDatabase()
        }
        Log.i("ss", array.value.toString())

        return array
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i(Keys.LOG_NAME, "main view model was cleared")
    }

    fun getEjtihadItem(): MutableLiveData<ArrayList<BookModel>> {
        val array = MutableLiveData<ArrayList<BookModel>>()
        array.value = arrayListOf<BookModel>().apply { allBooks.value }
        return array
    }

    fun getNososItem(): MutableLiveData<ArrayList<BookModel>> {
        val array = MutableLiveData<ArrayList<BookModel>>()
        array.value = arrayListOf<BookModel>().apply { allBooks }

        return array
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

