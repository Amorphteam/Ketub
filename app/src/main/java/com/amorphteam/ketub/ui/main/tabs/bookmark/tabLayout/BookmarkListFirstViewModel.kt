package com.amorphteam.ketub.ui.main.tabs.bookmark.tabLayout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkDatabaseDao
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkRepository
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.TempData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkListFirstViewModel(private val bookmarkDatabaseDao: BookmarkDatabaseDao) :
    ViewModel() {

    var startEpubAct = MutableLiveData<Boolean>()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository: BookmarkRepository = BookmarkRepository(bookmarkDatabaseDao)

    private var _allBookmarks = MutableLiveData<List<BookmarkModel>>()
    val allBookmarks: LiveData<List<BookmarkModel>>
        get() = _allBookmarks

    init {
        insertAllBookmarks()
        initializeBookmarks()
    }

    private suspend fun insert(bookmark: List<BookmarkModel>) {
        repository.insertDefaultData(bookmark)
    }

    private fun insertAllBookmarks() {
        uiScope.launch {
            insert(TempData.bookMarkArray)
        }
    }

    private fun initializeBookmarks() {
        uiScope.launch {
            _allBookmarks.value = getAllBookmarksFromDatabase()
            Log.i(Keys.LOG_NAME, "uiScope.launch.initializeBookmarks")
        }
    }

    private suspend fun getAllBookmarksFromDatabase(): List<BookmarkModel>? {
        Log.i(Keys.LOG_NAME, "getAllBookmarksFromDatabase")

        return withContext(Dispatchers.IO) {
            val bookmark = repository.getAllBookmarks()
            bookmark
        }
    }


    fun openEpubAct() {
        startEpubAct.value = true
    }

}